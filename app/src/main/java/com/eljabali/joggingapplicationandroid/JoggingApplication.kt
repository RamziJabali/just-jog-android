package com.eljabali.joggingapplicationandroid

import android.app.Application
import com.eljabali.joggingapplicationandroid.koin.calendarModule
import com.eljabali.joggingapplicationandroid.koin.mapsModule
import com.eljabali.joggingapplicationandroid.koin.networkModule
import com.eljabali.joggingapplicationandroid.koin.statisticsModule
import com.eljabali.joggingapplicationandroid.motivationalquotes.motivationalQuotesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class JoggingApplication : Application() {
    private val modules = listOf(
        calendarModule,
        statisticsModule,
        mapsModule,
        networkModule,
        motivationalQuotesModule
    )

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@JoggingApplication)
            modules(modules)
        }
    }
}