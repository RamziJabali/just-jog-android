package com.eljabali.joggingapplicationandroid.data.repo

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable

class WorkoutRepository(private val workoutDAO: WorkoutDAO) {

    fun getAllWorkoutDates(): Observable<List<WorkoutDate>> = workoutDAO.getAll()

    fun getRangeOfDates(startDate: String, endDate: String): Observable<List<WorkoutDate>> = workoutDAO.getByRangeOfDates(startDate, endDate)

    fun getWorkoutDate(date: String): Maybe<List<WorkoutDate>> = workoutDAO.getByDate(date)

    fun addWorkoutDate(workoutDate: WorkoutDate): Completable =
        workoutDAO.addUpdateWorkout(workoutDate)

    fun deleteAllWorkoutDates(): Completable = workoutDAO.deleteAll()
}