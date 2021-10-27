package com.eljabali.joggingapplicationandroid.data.repo.calendar

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

    @Query("SELECT * FROM jog_summary WHERE jog_start_date LIKE (:stringDate)")
    fun getByDate(stringDate: String): Maybe<List<JogSummary>>

    @Query("SELECT * FROM jog_summary WHERE jog_start_date BETWEEN (:startDate) AND (:endDate)")
    fun getByRangeOfDates(startDate: String, endDate: String): Observable<List<JogSummary>>

    @Query("DELETE FROM jog_summary")
    fun deleteAll(): Completable

    @Query("SELECT * FROM jog_summary ORDER BY jog_id DESC LIMIT 1")
    fun getLastJogDate(): Maybe<JogSummary>

    @Delete
    fun delete(jogSummary: JogSummary): Single<Int>
}