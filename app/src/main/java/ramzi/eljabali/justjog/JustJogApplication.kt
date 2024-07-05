package ramzi.eljabali.justjog

import android.Manifest
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.GlobalContext.startKoin
import ramzi.eljabali.justjog.koin.calendarModule
import ramzi.eljabali.justjog.koin.jogDataBaseModule
import ramzi.eljabali.justjog.koin.jogUseCaseModule
import ramzi.eljabali.justjog.koin.networkModule
import ramzi.eljabali.justjog.koin.statisticsModule

class JustJogApplication : Application() {
    private val CHANNEL_ID_1 = "JUST_JOG_1"
    private val modules =
        listOf(statisticsModule, jogDataBaseModule, jogUseCaseModule, networkModule, calendarModule)

    override fun onCreate() {
        super.onCreate()
        startKoin {
            AndroidLogger()
            androidContext(this@JustJogApplication)
            modules(modules)
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val name: String = getString(R.string.app_name)
        val mChannel =
            NotificationChannel(CHANNEL_ID_1, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = getString(R.string.channel_description)
            }
        notificationManager.createNotificationChannel(mChannel)
        Log.i("NotificationUtil::Class", "onCreate: Created Channel")
    }
}

object Permissions {
    val list =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
}
