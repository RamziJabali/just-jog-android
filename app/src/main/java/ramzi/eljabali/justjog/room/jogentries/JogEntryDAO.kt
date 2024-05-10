package ramzi.eljabali.justjog.room.jogentries

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

@Dao
interface JogEntryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUpdateWorkout(jogEntries: JogEntry): CompletableDeferred<Unit>

    @Query("SELECT * FROM jog_entries")
    suspend fun getAll(): Flow<List<JogEntry>>

    @Query("SELECT * FROM jog_entries WHERE date_time LIKE (:stringDate)")
    suspend fun getByDate(stringDate: String): Flow<List<JogEntry>?>

    @Query("SELECT * FROM jog_entries WHERE date_time BETWEEN (:startDate) AND (:endDate)")
    suspend fun getByRangeOfDates(startDate: String, endDate: String): Flow<List<JogEntry>>

    @Query("SELECT * FROM jog_entries WHERE jog_summary_id IS :jogId")
    suspend fun getByID(jogId: Int): Flow<List<JogEntry?>>

    @Query("DELETE FROM jog_entries")
    suspend fun deleteAll(): CompletableDeferred<Unit>
}