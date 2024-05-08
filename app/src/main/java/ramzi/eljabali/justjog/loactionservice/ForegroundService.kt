package ramzi.eljabali.justjog.loactionservice

import android.app.ForegroundServiceStartNotAllowedException
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import ramzi.eljabali.justjog.MainActivity
import ramzi.eljabali.justjog.R
import ramzi.eljabali.justjog.notification.permissions

class ForegroundService : Service() {
    companion object {
        private const val NOTIFICATION_ID = 1
        private const val LOCATION_REQUEST_INTERVAL_MS = 2000L
        private const val CHANNEL_ID_1 = "JUST_JOG_1"
    }

    private val locationManager by lazy {
        ContextCompat.getSystemService(application, LocationManager::class.java) as LocationManager
    }

    private val locationListener: LocationListener by lazy {
        LocationListener { location ->
            Log.d(
                "Log.d",
                "Time:${location.time}\nLatitude: ${location.latitude}, Longitude:${location.longitude}"
            )
        }
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> {
                Log.i("ForegroundService::Class", "Starting service")
                start()
            }

            Actions.STOP.toString() -> {
                Log.i("ForegroundService::Class", "Stopping service")
                stopSelf()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    // When onStartCommand intent confirms user request to start the service
    private fun start() {
        Log.i("ForegroundService::Class", "Checking permissions")
        for (permission in permissions.list) {
            val currentPermission = ContextCompat.checkSelfPermission(this, permission)
            if (currentPermission == PackageManager.PERMISSION_DENIED) {
                // Without these permissions the service cannot run in the foreground
                // Consider informing user or updating your app UI if visible.
                Log.d("ForegroundService::Class", "Permissions were not given, stopping service!")
                stopSelf()
            }
        }

        try {
            val notification = getNotification()
            ServiceCompat.startForeground(
                this,
                NOTIFICATION_ID, // Cannot be 0
                notification,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
                } else {
                    0
                },
            )
            // getting user location
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                LOCATION_REQUEST_INTERVAL_MS,
                0F,
                locationListener
            )
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


    // ~~~ Notification Handler ~~~
    private fun getNotification(): Notification {
        // Intent is created to move the user to the MainActivity when they click on the notification
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        //Pending Intent created using the Intent previously created in order to move
        // the user to the MainActivity when they click on the notification
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        val stopServicePendingIntent by lazy {
            PendingIntent.getActivity(
                this, 0, Intent(this, MainActivity::class.java).apply {
//                putExtra(HomeActivity.STOP_SERVICE_KEY, true)
                },
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

//        Intent(applicationContext, ForegroundService::class.java).also {
//            it.action = Actions.STOP.toString()
//            startService(it)
//        }

        return NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.mipmap.just_jog_icon_foreground)
            .setContentTitle(ContextCompat.getString(baseContext, R.string.just_jog))
            .setContentText(ContextCompat.getString(baseContext, R.string.notification_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setAutoCancel(false)
            // Set the intent that fires when the user taps the notification.
            .setContentIntent(pendingIntent)
            .addAction(
                R.mipmap.cross_foreground,
                getString(R.string.stop_jog),
                stopServicePendingIntent
            )
            .build()
    }

    // Actions
    enum class Actions {
        START,
        STOP
    }
}