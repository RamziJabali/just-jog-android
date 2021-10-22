package com.eljabali.joggingapplicationandroid.data.repo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eljabali.joggingapplicationandroid.data.repo.calendar.CalendarDAO
import com.eljabali.joggingapplicationandroid.data.repo.calendar.CalendarJogDate
import com.eljabali.joggingapplicationandroid.data.repo.jog.JogDAO
import com.eljabali.joggingapplicationandroid.data.repo.jog.JogDate

@Database(entities = [JogDate::class, CalendarJogDate::class], version = 1, exportSchema = false)
abstract class JogDatabase : RoomDatabase() {

    abstract fun jogDAO(): JogDAO
    abstract fun calendarDAO(): CalendarDAO

    companion object {
        @Volatile
        private var INSTANCE: JogDatabase? = null

        fun getInstance(context: Context): JogDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        JogDatabase::class.java,
                        "user_jog_schedule"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}