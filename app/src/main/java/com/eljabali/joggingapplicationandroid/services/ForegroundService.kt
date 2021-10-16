package com.eljabali.joggingapplicationandroid.services

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.eljabali.joggingapplicationandroid.R
import com.eljabali.joggingapplicationandroid.services.NotificationChannels.ACTIVE_RUN
import com.eljabali.joggingapplicationandroid.calendar.mainview.MainActivity
import com.eljabali.joggingapplicationandroid.data.usecase.ModifiedJogDateInformation
import com.eljabali.joggingapplicationandroid.data.usecase.UseCase
import com.eljabali.joggingapplicationandroid.statistics.viewmodel.StatisticsViewModel
import com.eljabali.joggingapplicationandroid.util.getFormattedTime
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import zoneddatetime.ZonedDateTimes
import java.time.Duration
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class ForegroundService : Service() {

    companion object {
        const val NOTIFICATION_ID = 1
        const val STOP_SERVICE_KEY = "STOP_SERVICE_KEY"
    }

    private val locationManager by lazy {
        ContextCompat.getSystemService(application, LocationManager::class.java) as LocationManager
    }
    private val useCase by inject<UseCase>()
    private val compositeDisposable = CompositeDisposable()
    private val jogStart by lazy { ZonedDateTimes.now }
    private val pendingIntent by lazy {
        PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), 0)
    }
    private val stopServicePendingIntent by lazy {
        PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java).apply {
            putExtra(STOP_SERVICE_KEY, true)
        },
                PendingIntent.FLAG_CANCEL_CURRENT)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        Observable.interval(1, TimeUnit.SECONDS)
                .timeInterval()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val duration = Duration.between(jogStart, ZonedDateTimes.now)
                    val time = getFormattedTime(duration.seconds)
                    startForeground(NOTIFICATION_ID, createNotification(time))
                }
                .addTo(compositeDisposable)
        startForeground(NOTIFICATION_ID, createNotification(""))
        startTrackingJog()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        compositeDisposable.clear()
    }

    private fun startTrackingJog() {
        useCase.getNewRunID()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { currentRunId -> jogListener(currentRunId) },
                        { error -> Log.e(StatisticsViewModel.SVM_TAG, error.localizedMessage, error) },
                        { jogListener(1) }
                ).addTo(compositeDisposable)
    }

    private fun jogListener(id: Int) {
        if (ActivityCompat.checkSelfPermission(
                        application,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        application,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        )
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0F) { location ->
                recordRunEvent(id, location)
            }
    }

    private fun recordRunEvent(id: Int, location: Location) {
        Log.i(StatisticsViewModel.SVM_TAG, "${location.latitude} Success")
        var latLng = LatLng(location.latitude, location.longitude)
        val modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now(),
                latitudeLongitude = latLng,
                runNumber = id
        )
        addJog(modifiedJogDateInformation)
    }

    private fun addJog(modifiedJogDateInformation: ModifiedJogDateInformation) {
        useCase.addJog(modifiedJogDateInformation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            Log.i(UseCase.UC_TAG, "Success")
                        },
                        { error -> Log.e(UseCase.UC_TAG, error.localizedMessage, error) })
                .addTo(compositeDisposable)
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        with(ACTIVE_RUN) {
            val channel = NotificationChannel(
                    channelId,
                    baseContext.getString(channelName),
                    channelImportance
            ).apply {
                description = baseContext.getString(channelDescription)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(contentText: String): Notification =
            NotificationCompat.Builder(this, ACTIVE_RUN.channelId)
                    .setContentTitle(baseContext.getString(R.string.active_run))
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setContentText(contentText)
                    .setSmallIcon(R.drawable.ic_action_directions_run)
                    .setContentIntent(pendingIntent)
                    .addAction(android.R.drawable.checkbox_off_background, getString(R.string.stop), stopServicePendingIntent)
                    .build()


}