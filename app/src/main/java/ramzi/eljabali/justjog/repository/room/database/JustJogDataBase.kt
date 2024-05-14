package ramzi.eljabali.justjog.repository.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ramzi.eljabali.justjog.repository.room.jogentries.JogEntry
import ramzi.eljabali.justjog.repository.room.jogentries.JogEntryDAO
import ramzi.eljabali.justjog.repository.room.jogsummary.JogSummary
import ramzi.eljabali.justjog.repository.room.jogsummary.JogSummaryDAO
import ramzi.eljabali.justjog.repository.room.jogsummarytemp.JogSummaryTemp
import ramzi.eljabali.justjog.repository.room.jogsummarytemp.JogSummaryTempDAO

@Database(entities = [JogEntry::class, JogSummary::class, JogSummaryTemp::class], version = 1, exportSchema = false)
abstract class JustJogDataBase: RoomDatabase() {
    abstract fun jogEntryDao(): JogEntryDAO
    abstract fun jogSummaryDao(): JogSummaryDAO
    abstract fun jogSummaryTempDao(): JogSummaryTempDAO
}