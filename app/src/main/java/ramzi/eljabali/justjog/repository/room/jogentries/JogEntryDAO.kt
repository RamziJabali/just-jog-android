package ramzi.eljabali.justjog.repository.room.jogentries

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow

@Dao
interface JogEntryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUpdateWorkout(jogEntries: JogEntry)

    @Query("SELECT * FROM jog_entries")
    fun getAll(): Flow<List<JogEntry>>

    @Query("SELECT * FROM jog_entries WHERE date_time LIKE (:stringDate)")
    fun getByDate(stringDate: String): Flow<List<JogEntry>?>

    @Query("SELECT * FROM jog_entries WHERE date_time BETWEEN (:startDate) AND (:endDate)")
    fun getByRangeOfDates(startDate: String, endDate: String): Flow<List<JogEntry>>

    @Query("SELECT * FROM jog_entries WHERE jog_summary_id IS :jogId")
    fun getByID(jogId: Int): Flow<List<JogEntry?>>

    @Query("DELETE FROM jog_entries")
    fun deleteAll()
}