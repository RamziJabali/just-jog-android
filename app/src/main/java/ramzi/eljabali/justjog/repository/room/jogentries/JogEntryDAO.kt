package ramzi.eljabali.justjog.repository.room.jogentries

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface JogEntryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addEntry(jogEntries: JogEntry)

    @Query("SELECT * FROM jog_entries")
    fun getAll(): Flow<List<JogEntry>>

    @Query("SELECT * FROM jog_entries WHERE date_time LIKE (:stringDate)")
    fun getByDate(stringDate: String): Flow<List<JogEntry>>

    @Query("SELECT * FROM jog_entries WHERE date_time BETWEEN (:startDate) AND (:endDate)")
    fun getByDateRange(startDate: String, endDate: String): Flow<List<JogEntry>>

    @Query("DELETE FROM jog_entries")
    fun deleteAll()
}