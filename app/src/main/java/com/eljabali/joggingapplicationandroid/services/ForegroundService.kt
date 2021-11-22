package com.eljabali.joggingapplicationandroid.services

import android.annotation.SuppressLint
import android.app.*
import android.app.NotificationChannel
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.eljabali.joggingapplicationandroid.R
import com.eljabali.joggingapplicationandroid.data.usecase.JogUseCase
import com.eljabali.joggingapplicationandroid.data.usecase.ModifiedJogDateInformation
import com.eljabali.joggingapplicationandroid.home.HomeActivity
import com.eljabali.joggingapplicationandroid.services.NotificationChannel.ACTIVE_RUN
import com.eljabali.joggingapplicationandroid.util.*
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
        private const val NOTIFICATION_ID = 1
    }

    private var id: Int = 0
    private val locationManager by lazy {
        ContextCompat.getSystemService(application, LocationManager::class.java) as LocationManager
    }

    private val locationListener: LocationListener by lazy {
        LocationListener { location ->
            recordRunEvent(id, location)
        }
    }
    private val jogUseCase by inject<JogUseCase>()
    private val compositeDisposable = CompositeDisposable()
    private val jogStartZonedDateTime by lazy { ZonedDateTimes.now }
    private val pendingIntent by lazy {
        PendingIntent.getActivity(this, 0, Intent(this, HomeActivity::class.java), 0)
    }
    private val stopServicePendingIntent by lazy {
        PendingIntent.getActivity(
            this, 0, Intent(this, HomeActivity::class.java).apply {
                putExtra(HomeActivity.STOP_SERVICE_KEY, true)
            },
            PendingIntent.FLAG_CANCEL_CURRENT
        )
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
                val duration = Duration.between(jogStartZonedDateTime, ZonedDateTimes.now)
                val time = getFormattedTime(duration.seconds, DurationFormat.HH_MM_SS)
                startForeground(NOTIFICATION_ID, createNotification(time))
            }
            .addTo(compositeDisposable)
        startForeground(NOTIFICATION_ID, createNotification(""))
        startTrackingJog()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        jogUseCase.getJogEntriesById(id)
            .map { lisOfJogs ->
                addJogSummary(
                    lisOfJogs[0].dateTime,
                    lisOfJogs.last().dateTime,
                    id,
                    getTotalDistance(getListOfLatLngFromModifiedJogEntry(lisOfJogs))
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    compositeDisposable.clear()
                    locationManager.removeUpdates(locationListener)
                    super.onDestroy()
                },
                { error ->
                    Log.e(TAG, error.localizedMessage, error)
                    compositeDisposable.clear()
                    locationManager.removeUpdates(locationListener)
                    super.onDestroy()
                }
            )
            .addTo(compositeDisposable)
    }

    private fun getListOfLatLngFromModifiedJogEntry(lisOfJogs: List<ModifiedJogDateInformation>): List<LatLng> {
        val listOfLatLng = mutableListOf<LatLng>()
        lisOfJogs.forEach{ jog ->
            listOfLatLng.add(jog.latitudeLongitude)
        }
        return listOfLatLng
    }

    private fun startTrackingJog() {
        jogUseCase.getNewRunID()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { currentRunId -> jogListener(currentRunId) },
                { error -> Log.e(TAG, error.localizedMessage, error) },
                { jogListener(1) }
            ).addTo(compositeDisposable)
    }

    @SuppressLint("MissingPermission")
    private fun jogListener(id: Int) {
        if (!PermissionUtil.isGpsLocationGranted(applicationContext)) {
            Log.e(TAG, "Don't have permissions")
            return
        }
        this.id = id
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            2000,
            0F,
            locationListener
        )
    }

    private fun recordRunEvent(id: Int, location: Location) {
        Log.i(TAG, "Success getting location")
        val modifiedJogDateInformation = ModifiedJogDateInformation(
            dateTime = ZonedDateTime.now(),
            latitudeLongitude = LatLng(location.latitude, location.longitude),
            runNumber = id
        )
        addJog(modifiedJogDateInformation)
    }

    private fun addJog(modifiedJogDateInformation: ModifiedJogDateInformation) {
        jogUseCase.addJog(modifiedJogDateInformation)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.i(TAG, "Success")
                },
                { error -> Log.e(TAG, error.localizedMessage, error) })
            .addTo(compositeDisposable)
    }

    private fun addJogSummary(
        startDateTime: ZonedDateTime,
        endDateTime: ZonedDateTime,
        jogId: Int,
        totalJogDistance: Double
    ) {
        jogUseCase.addJogSummary(startDateTime, jogId, endDateTime, totalJogDistance)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.i(TAG, "Success")
                },
                { error -> Log.e(TAG, error.localizedMessage, error) })
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
            .addAction(
                android.R.drawable.checkbox_off_background,
                getString(R.string.stop_jog),
                stopServicePendingIntent
            )
            .build()
}