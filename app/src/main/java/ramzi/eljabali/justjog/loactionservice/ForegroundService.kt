package ramzi.eljabali.justjog.loactionservice

import android.Manifest
import android.app.ForegroundServiceStartNotAllowedException
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import javatimefun.zoneddatetime.ZonedDateTimes
import javatimefun.zoneddatetime.extensions.print
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ramzi.eljabali.justjog.MainActivity
import ramzi.eljabali.justjog.R
import ramzi.eljabali.justjog.notification.permissions
import ramzi.eljabali.justjog.usecase.JogUseCase
import ramzi.eljabali.justjog.util.DateFormat
import ramzi.eljabali.justjog.util.TAG
import ramzi.eljabali.justjog.util.formatDuration
import ramzi.eljabali.justjog.workmanager.JogSummaryWorkManager
import java.time.Duration
import java.time.ZonedDateTime

class ForegroundService : Service() {
    companion object {
        private const val NOTIFICATION_ID = 1
        private const val LOCATION_REQUEST_INTERVAL_MS = 1000L
        private const val CHANNEL_ID_1 = "JUST_JOG_1"
    }

    private val jogUseCase by inject<JogUseCase>()
    private val jogStartZonedDateTime by lazy { ZonedDateTimes.now }

    private val locationManager by lazy {
        ContextCompat.getSystemService(application, LocationManager::class.java) as LocationManager
    }
    private var id = 0
    private val locationListener: LocationListener by lazy {
        LocationListener { location ->
            val duration = Duration.between(jogStartZonedDateTime, ZonedDateTime.now())
            updateNotification(formatDuration(duration))
            recordRunEvent(id, location)
        }
    }

    private val pendingIntent: PendingIntent by lazy {
        PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private val stopServicePendingIntent: PendingIntent by lazy {
        PendingIntent.getService(
            this, 0, Intent(this, ForegroundService::class.java).apply {
                action = Actions.STOP.name
            },
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.name -> {
                Log.i("ForegroundService::Class", "Starting service")
                start()
            }

            Actions.STOP.name -> {
                Log.i("ForegroundService::Class", "Stopping service")
                stop()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun stop() {
        locationManager.removeUpdates(locationListener)
        stopSelf()
    }

    private fun start() {
        Log.i("ForegroundService::Class", "Checking permissions")
        for (permission in permissions.list) {
            val currentPermission = ContextCompat.checkSelfPermission(this, permission)
            if (currentPermission == PackageManager.PERMISSION_DENIED) {
                // Without these permissions the service cannot run in the foreground
                // Consider informing user or updating your app UI if visible.
                Log.e("ForegroundService::Class", "Permissions were not given, stopping service!")
                stopSelf()
            }
        }

        try {
            ServiceCompat.startForeground(
                this,
                NOTIFICATION_ID, // Cannot be 0
                getNotification(""),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
                } else {
                    0
                },
            )
            getJogIDAndStartTrackingJog()
        } catch (e: Exception) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && e is ForegroundServiceStartNotAllowedException
            ) {
                // App not in a valid state to start foreground service
                // (e.g. started from bg)
                Log.d("ForegroundService::Class", e.message.toString())
            }
        }
    }


    // ~~~ Notification Builder ~~~
    private fun getNotification(time: String) =
        NotificationCompat.Builder(applicationContext, CHANNEL_ID_1)
            .setSmallIcon(R.mipmap.just_jog_icon_foreground)
            .setContentTitle(ContextCompat.getString(applicationContext, R.string.just_jog))
            .setContentText(time)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setAutoCancel(false)
            .setStyle(NotificationCompat.BigTextStyle())
            .setOnlyAlertOnce(true)
            // Set the intent that fires when the user taps the notification.
            .setContentIntent(pendingIntent)
            .addAction(
                R.mipmap.cross_monochrome,
                getString(R.string.stop_jog),
                stopServicePendingIntent
            )
            .build()

    private fun updateNotification(content: String) {
        val notification = getNotification(content)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    // jog
    private fun getJogIDAndStartTrackingJog() {
        // Launching a coroutine on the main dispatcher to collect the Flow
        MainScope().launch {
            try {
                Log.i(TAG, "Getting new run ID")
                jogUseCase.getNewRunID().collect { newId ->
                    Log.i(TAG, "Got new run id $newId")
                    // Proceed with starting the tracking jog
                    jogListener(newId)
                }
            } catch (e: Exception) {
                // Handle the error
                Log.e(TAG, "Error getting new run ID", e)
            }
        }
    }

    private fun jogListener(newId: Int) {
        // getting user location
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        id = newId
        Log.i(TAG, "Starting location updates")
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            LOCATION_REQUEST_INTERVAL_MS,
            0F,
            locationListener
        )
    }

    private fun recordRunEvent(id: Int, location: Location) {
        Log.i(TAG, "Success getting location")
        val data = Data.Builder().apply {
            putDouble("KEY_LONGITUDE", location.longitude)
            putDouble("KEY_LATITUDE", location.latitude)
            putString("DATE_TIME", ZonedDateTime.now().print(DateFormat.YYYY_MM_DD_T_TIME.format))
            putInt("ID", id)
        }.build()

        val recordSummaryWorker =
            OneTimeWorkRequest.Builder(JogSummaryWorkManager::class.java).apply {
                setInputData(data)
            }

        WorkManager.getInstance(applicationContext).enqueue(recordSummaryWorker.build())
    }

    // Actions
    enum class Actions {
        START,
        STOP
    }
}