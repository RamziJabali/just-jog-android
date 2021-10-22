package com.eljabali.joggingapplicationandroid.data.repo.jog

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable

class JogRepository(private val jogDAO: JogDAO) {

    fun getAllWorkoutDates(): Observable<List<JogDate>> = jogDAO.getAll()

    fun getRangeOfDates(startDate: String, endDate: String): Observable<List<JogDate>> =
        jogDAO.getByRangeOfDates(startDate, endDate)

    fun getWorkoutDate(date: String): Maybe<List<JogDate>> = jogDAO.getByDate(date)

    fun addWorkoutDate(jogDate: JogDate): Completable =
        jogDAO.addUpdateWorkout(jogDate)

    fun deleteAllWorkoutDates(): Completable = jogDAO.deleteAll()

    fun getLastRunID(): Maybe<JogDate> = jogDAO.getLastRunID()
}