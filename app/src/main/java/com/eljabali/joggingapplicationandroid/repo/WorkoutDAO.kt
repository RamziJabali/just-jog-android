package com.eljabali.joggingapplicationandroid.repo

import androidx.room.*
import com.eljabali.joggingapplicationandroid.repo.WorkoutDate
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface WorkoutDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUpdateWorkout(workoutDate: WorkoutDate): Completable

    @Query("SELECT * FROM user_workout_schedule")
    fun getAll(): Observable<List<WorkoutDate>>

    @Query("SELECT * FROM user_workout_schedule WHERE date_time IN (:dateInMillis)")
    fun getByDate(dateInMillis: Long): Maybe<List<WorkoutDate>>

    @Query("DELETE FROM user_workout_schedule")
    fun deleteAll(): Completable

    @Delete
    fun delete(workoutDate: WorkoutDate): Single<Int>
}