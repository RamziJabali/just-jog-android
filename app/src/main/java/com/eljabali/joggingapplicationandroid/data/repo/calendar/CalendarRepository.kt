package com.eljabali.joggingapplicationandroid.data.repo.calendar

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable

class CalendarRepository(private val calendarDAO: CalendarDAO) {
    fun getAllCalendarDates(): Observable<List<CalendarJogDate>> = calendarDAO.getAll()

    fun getRangeOfJogDates(startDate: String, endDate: String): Observable<List<CalendarJogDate>> =
        calendarDAO.getByRangeOfDates(startDate, endDate)

    fun getJogDate(date: String): Maybe<List<CalendarJogDate>> = calendarDAO.getByDate(date)

    fun addJogDate(calendarJogDate: CalendarJogDate): Completable =
        calendarDAO.addJogDate(calendarJogDate)

    fun deleteAllJogDates(): Completable = calendarDAO.deleteAll()

    fun getLastJogDate(): Maybe<CalendarJogDate> = calendarDAO.getLastJogDate()
}