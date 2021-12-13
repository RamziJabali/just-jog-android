package com.eljabali.joggingapplicationandroid.data.repo.jogsummarytemp

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.eljabali.joggingapplicationandroid.data.repo.jogentries.JogEntries
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
interface JogSummaryTempDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addJogDate(jogSummaryTemp: JogSummaryTemp): Completable

    @Query("SELECT * FROM jog_summary_temp")
    fun getAll(): Observable<List<JogSummaryTemp>>

//    @Query("SELECT * FROM jog_summary WHERE CAST(start_date_time AS DATE) BETWEEN CAST(:startDate AS DATE) AND CAST(:endDate AS DATE)")
//    fun getByRangeOfDates(startDate: String, endDate: String): Observable<List<JogSummary>>

    @Query("SELECT * FROM jog_summary_temp WHERE id IS :jogId")
    fun getByID(jogId: Int): Maybe<JogSummaryTemp>

    @Query("DELETE FROM jog_summary_temp")
    fun deleteAll(): Completable

    @Query("SELECT * FROM jog_summary_temp ORDER BY id DESC LIMIT 1")
    fun getLast(): Maybe<JogSummaryTemp>

    @Delete
    fun delete(jogSummaryTemp: JogSummaryTemp): Single<Int>
}