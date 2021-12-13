package com.eljabali.joggingapplicationandroid.data.repo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eljabali.joggingapplicationandroid.data.repo.jogentries.JogEntries
import com.eljabali.joggingapplicationandroid.data.repo.jogentries.JogEntriesDAO
import com.eljabali.joggingapplicationandroid.data.repo.jogsummary.JogSummary
import com.eljabali.joggingapplicationandroid.data.repo.jogsummary.JogSummaryDAO
import com.eljabali.joggingapplicationandroid.data.repo.jogsummarytemp.JogSummaryTemp
import com.eljabali.joggingapplicationandroid.data.repo.jogsummarytemp.JogSummaryTempDAO

@Database(
    entities = [JogEntries::class, JogSummary::class, JogSummaryTemp::class],
    version = 1,
    exportSchema = false
)
abstract class JogDatabase : RoomDatabase() {

    abstract fun jogDAO(): JogEntriesDAO
    abstract fun jogSummaryDAO(): JogSummaryDAO
    abstract fun jogSummaryTempDAO(): JogSummaryTempDAO

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
                        "TOTAL_JOGS"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}