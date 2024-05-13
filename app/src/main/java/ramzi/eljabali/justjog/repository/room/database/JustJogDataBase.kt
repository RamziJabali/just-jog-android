package ramzi.eljabali.justjog.repository.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ramzi.eljabali.justjog.repository.room.jogentries.JogEntry
import ramzi.eljabali.justjog.repository.room.jogentries.JogEntryDAO

@Database(entities = [JogEntry::class], version = 1, exportSchema = false)
abstract class JustJogDataBase: RoomDatabase() {
    abstract fun jogEntryDao(): JogEntryDAO
}