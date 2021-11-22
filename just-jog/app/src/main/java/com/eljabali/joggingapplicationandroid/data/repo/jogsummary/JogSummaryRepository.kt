package com.eljabali.joggingapplicationandroid.data.repo.jogsummary

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import java.time.ZonedDateTime

class JogSummaryRepository(private val jogSummaryDAO: JogSummaryDAO) {
    fun getAll(): Observable<List<JogSummary>> = jogSummaryDAO.getAll()

    fun getByRangeOfDates(startDate: ZonedDateTime, endDate: ZonedDateTime): Observable<List<JogSummary>> =
        jogSummaryDAO.getBetweenDatesUseCase(startDate, endDate)

    fun getByDate(date: String): Maybe<List<JogSummary>> = jogSummaryDAO.getByDate(date)

    fun add(jogSummary: JogSummary): Completable =
        jogSummaryDAO.addJogDate(jogSummary)

    fun deleteAll(): Completable = jogSummaryDAO.deleteAll()

    fun getLast(): Maybe<JogSummary> = jogSummaryDAO.getLast()
}