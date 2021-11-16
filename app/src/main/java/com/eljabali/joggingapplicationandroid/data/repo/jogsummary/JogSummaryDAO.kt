package com.eljabali.joggingapplicationandroid.data.repo.jogsummary

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.eljabali.joggingapplicationandroid.util.DateFormat
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import zoneddatetime.extensions.getDayDifference
import zoneddatetime.extensions.isAfterDay
import zoneddatetime.extensions.print
import java.time.ZonedDateTime

@Dao
interface JogSummaryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addJogDate(jogSummary: JogSummary): Completable

    @Query("SELECT * FROM jog_summary")
    fun getAll(): Observable<List<JogSummary>>

    @Query("SELECT * FROM jog_summary WHERE start_date_time LIKE (:stringDate)")
    fun getByDate(stringDate: String): Maybe<List<JogSummary>>

//    @Query("SELECT * FROM jog_summary WHERE CAST(start_date_time AS DATE) BETWEEN CAST(:startDate AS DATE) AND CAST(:endDate AS DATE)")
//    fun getByRangeOfDates(startDate: String, endDate: String): Observable<List<JogSummary>>

    @RawQuery(observedEntities = [JogSummary::class])
    fun getBetweenDatesInclusive(query: SupportSQLiteQuery): Observable<List<JogSummary>>

    fun getBetweenDatesUseCase(
        fromDate: ZonedDateTime,
        toDate: ZonedDateTime
    ): Observable<List<JogSummary>> {
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
    fun deleteAll(): Completable

    @Query("SELECT * FROM jog_summary ORDER BY id DESC LIMIT 1")
    fun getLast(): Maybe<JogSummary>

    @Delete
    fun delete(jogSummary: JogSummary): Single<Int>
}