package com.eljabali.joggingapplicationandroid.data.repo.jog

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface JogDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUpdateWorkout(jogDate: JogDate): Completable

    @Query("SELECT * FROM user_jog_schedule")
    fun getAll(): Observable<List<JogDate>>

    //    @Query("SELECT * FROM user_workout_schedule WHERE date IN (:stringDate)")
    @Query("SELECT * FROM user_jog_schedule WHERE date_time LIKE (:stringDate)")
    fun getByDate(stringDate: String): Maybe<List<JogDate>>

    @Query("SELECT * FROM user_jog_schedule WHERE date_time BETWEEN (:startDate) AND (:endDate)")
    fun getByRangeOfDates(startDate: String, endDate: String): Observable<List<JogDate>>

    @Query("DELETE FROM user_jog_schedule")
    fun deleteAll(): Completable

    @Query("SELECT * FROM user_jog_schedule ORDER BY id DESC LIMIT 1")
    fun getLastRunID(): Maybe<JogDate>

    @Delete
    fun delete(jogDate: JogDate): Single<Int>
}