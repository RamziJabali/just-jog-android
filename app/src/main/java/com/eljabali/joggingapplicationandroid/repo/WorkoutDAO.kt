package com.eljabali.joggingapplicationandroid.repo

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface WorkoutDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUpdateWorkout(workoutDate: WorkoutDate): Completable

    @Query("SELECT * FROM user_jog_schedule")
    fun getAll(): Observable<List<WorkoutDate>>

    //    @Query("SELECT * FROM user_workout_schedule WHERE date IN (:stringDate)")
    @Query("SELECT * FROM user_jog_schedule WHERE date_time LIKE (:stringDate)")
    fun getByDate(stringDate: String): Maybe<List<WorkoutDate>>

    @Query("SELECT * FROM user_jog_schedule WHERE date_time BETWEEN (:startDate) AND (:endDate)")
    fun getByRangeOfDates(startDate: String, endDate: String): Observable<List<WorkoutDate>>

    @Query("DELETE FROM user_jog_schedule")
    fun deleteAll(): Completable

    @Delete
    fun delete(workoutDate: WorkoutDate): Single<Int>
}