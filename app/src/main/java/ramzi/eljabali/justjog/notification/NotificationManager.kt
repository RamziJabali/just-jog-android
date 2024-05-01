package ramzi.eljabali.justjog.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getString
import androidx.core.content.ContextCompat.getSystemService
import ramzi.eljabali.justjog.R
import ramzi.eljabali.justjog.MainActivity

class NotificationManager(private val mContext: Context) {
    // Intent is created to move the user to the MainActivity when they click on the notification
    private val intent = Intent(mContext, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    //need a notification manager
    private val notificationManager =
        getSystemService(mContext, NotificationManager::class.java) as NotificationManager

    //Pending Intent created using the Intent previously created in order to move
    // the user to the MainActivity when they click on the notification
    private val pendingIntent: PendingIntent =
        PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    private val CHANNEL_ID_1 = "JUST_JOG_1"

    fun createNotificationChannels() {
        val name: String = getString(mContext, R.string.just_jog)
        val mChannel =
            NotificationChannel(CHANNEL_ID_1, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description =
                    "This is your personal notification Channel"
            }
        notificationManager.createNotificationChannel(mChannel)
    }

    fun notifyUser() {
        val notification = NotificationCompat.Builder(mContext, CHANNEL_ID_1)
            .setSmallIcon(R.mipmap.just_jog_icon_foreground)
            .setContentTitle(getString(mContext, R.string.just_jog))
            .setContentText(getString(mContext, R.string.notification_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that fires when the user taps the notification.
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        if (notificationManager.areNotificationsEnabled()) {
            notificationManager.notify(1, notification)
        }
    }
}