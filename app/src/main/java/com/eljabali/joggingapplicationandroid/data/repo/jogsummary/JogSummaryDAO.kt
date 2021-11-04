package com.eljabali.joggingapplicationandroid.data.repo.jogsummary

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface JogSummaryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addJogDate(jogSummary: JogSummary): Completable

    @Query("SELECT * FROM jog_summary")
    fun getAll(): Observable<List<JogSummary>>

    @Query("SELECT * FROM jog_summary WHERE start_date_time LIKE (:stringDate)")
    fun getByDate(stringDate: String): Maybe<List<JogSummary>>

    @Query("SELECT * FROM jog_summary WHERE CAST(start_date_time AS DATE) BETWEEN CAST(:startDate AS DATE) AND CAST(:endDate AS DATE)")
    fun getByRangeOfDates(startDate: String, endDate: String): Observable<List<JogSummary>>

    @Query("DELETE FROM jog_summary")
    fun deleteAll(): Completable

    @Query("SELECT * FROM jog_summary ORDER BY id DESC LIMIT 1")
    fun getLast(): Maybe<JogSummary>

    @Delete
    fun delete(jogSummary: JogSummary): Single<Int>
}