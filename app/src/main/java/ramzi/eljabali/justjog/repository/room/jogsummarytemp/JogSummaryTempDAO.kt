package ramzi.eljabali.justjog.repository.room.jogsummarytemp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface JogSummaryTempDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(jogSummaryTemp: JogSummaryTemp)

    @Query("SELECT * FROM jog_summary_temp")
    fun getAll(): Flow<List<JogSummaryTemp>>

    @Query("SELECT * FROM jog_summary_temp WHERE id IS :jogId")
    fun getById(jogId: Int): Flow<JogSummaryTemp?>

    @Query("DELETE FROM jog_summary_temp")
    fun deleteAll()

    @Query("SELECT * FROM jog_summary_temp ORDER BY id DESC LIMIT 1")
    fun getLast(): Flow<JogSummaryTemp?>

    @Delete
    fun delete(jogSummaryTemp: JogSummaryTemp): Int
}