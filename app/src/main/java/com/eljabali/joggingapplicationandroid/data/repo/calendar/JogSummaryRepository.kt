package com.eljabali.joggingapplicationandroid.data.repo.calendar

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable

class JogSummaryRepository(private val jogSummaryDAO: JogSummaryDAO) {
    fun getAllJogSummaries(): Observable<List<JogSummary>> = jogSummaryDAO.getAll()

    fun getRangeOfJogDates(startDate: String, endDate: String): Observable<List<JogSummary>> =
        jogSummaryDAO.getByRangeOfDates(startDate, endDate)

    fun getJogDate(date: String): Maybe<List<JogSummary>> = jogSummaryDAO.getByDate(date)

    fun addJogDate(jogSummary: JogSummary): Completable =
        jogSummaryDAO.addJogDate(jogSummary)

    fun deleteAllJogDates(): Completable = jogSummaryDAO.deleteAll()

    fun getLastJogDate(): Maybe<JogSummary> = jogSummaryDAO.getLastJogDate()
}