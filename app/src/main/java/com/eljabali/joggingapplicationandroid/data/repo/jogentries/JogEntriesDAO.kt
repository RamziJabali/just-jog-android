package com.eljabali.joggingapplicationandroid.data.repo.jogentries

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface JogEntriesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUpdateWorkout(jogEntries: JogEntries): Completable

    @Query("SELECT * FROM jog_entries")
    fun getAll(): Observable<List<JogEntries>>

    //    @Query("SELECT * FROM user_workout_schedule WHERE date IN (:stringDate)")
    @Query("SELECT * FROM jog_entries WHERE date_time LIKE (:stringDate)")
    fun getByDate(stringDate: String): Maybe<List<JogEntries>>

    @Query("SELECT * FROM jog_entries WHERE date_time BETWEEN (:startDate) AND (:endDate)")
    fun getByRangeOfDates(startDate: String, endDate: String): Observable<List<JogEntries>>

    @Query("DELETE FROM jog_entries")
    fun deleteAll(): Completable

    @Delete
    fun delete(jogEntries: JogEntries): Single<Int>
}