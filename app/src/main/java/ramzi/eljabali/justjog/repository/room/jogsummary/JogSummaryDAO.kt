package ramzi.eljabali.justjog.repository.room.jogsummary

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import javatimefun.zoneddatetime.extensions.getDayDifference
import javatimefun.zoneddatetime.extensions.isAfterDay
import javatimefun.zoneddatetime.extensions.print
import kotlinx.coroutines.flow.Flow
import ramzi.eljabali.justjog.util.DateFormat
import java.time.ZonedDateTime

@Dao
interface JogSummaryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addJogDate(jogSummary: JogSummary)

    @Query("SELECT * FROM jog_summary")
    fun getAll(): Flow<List<JogSummary>>

    @Query("SELECT * FROM jog_summary WHERE start_date_time LIKE (:stringDate)")
    fun getByDate(stringDate: String): Flow<List<JogSummary>?>

//    @Query("SELECT * FROM jog_summary WHERE CAST(start_date_time AS DATE) BETWEEN CAST(:startDate AS DATE) AND CAST(:endDate AS DATE)")
//    fun getByRangeOfDates(startDate: String, endDate: String): Observable<List<JogSummary>>

    @RawQuery(observedEntities = [JogSummary::class])
    fun getBetweenDatesInclusive(query: SupportSQLiteQuery): Flow<List<JogSummary>>

    fun getBetweenDatesUseCase(
        fromDate: ZonedDateTime,
        toDate: ZonedDateTime
    ): Flow<List<JogSummary>> {
        if (fromDate.isAfterDay(toDate)) {
            throw Exception("Illegal Start Date")
        }
        val differenceBetweenStartEndDate = fromDate.getDayDifference(toDate)
        var query = "SELECT * FROM jog_summary WHERE "
        if (differenceBetweenStartEndDate == 0L) {
            query += "start_date_time LIKE '${fromDate.print(DateFormat.YYYY_MM_DD.format)}%'"
            return getBetweenDatesInclusive(SimpleSQLiteQuery(query, arrayOf<JogSummary>()))
        }
        // for loop
        for (index in 0..differenceBetweenStartEndDate) {
            query += "start_date_time LIKE '${fromDate.plusDays(index).print(DateFormat.YYYY_MM_DD.format)}%'"
            if (index < differenceBetweenStartEndDate) {
                query += " OR "
            }
        }
        // CAST(start_date_time AS DATE) BETWEEN CAST(:startDate AS DATE) AND CAST(:endDate AS DATE)"
        return getBetweenDatesInclusive(SimpleSQLiteQuery(query, arrayOf<JogSummary>()))
    }


    @Query("DELETE FROM jog_summary")
    fun deleteAll()

    @Query("SELECT * FROM jog_summary ORDER BY id DESC LIMIT 1")
    fun getLast(): Flow<JogSummary?>

    @Delete
    fun delete(jogSummary: JogSummary)
}