package com.eljabali.joggingapplicationandroid.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.eljabali.joggingapplicationandroid.R
import com.eljabali.joggingapplicationandroid.services.NotificationChannels.ACTIVE_RUN
import com.eljabali.joggingapplicationandroid.calendar.mainview.MainActivity
import com.eljabali.joggingapplicationandroid.util.getFormattedTime
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import zoneddatetime.ZonedDateTimes
import java.time.Duration
import java.util.concurrent.TimeUnit

class ForegroundService : Service() {

    companion object {
        const val NOTIFICATION_ID = 1
    }

    private val compositeDisposable = CompositeDisposable()
    private val jogStart by lazy { ZonedDateTimes.now }
    private val pendingIntent by lazy {
        PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), 0)
    }
    private val stopServicePendingIntent by lazy {
        PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_CANCEL_CURRENT)
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
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        compositeDisposable.clear()
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