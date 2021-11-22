package com.eljabali.joggingapplicationandroid.data.repo.jogentries

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable

class JogEntriesRepository(private val jogEntriesDAO: JogEntriesDAO) {

    fun getAll(): Observable<List<JogEntries>> = jogEntriesDAO.getAll()

    fun getByRangeOfDates(startDate: String, endDate: String): Observable<List<JogEntries>> =
        jogEntriesDAO.getByRangeOfDates(startDate, endDate)

    fun getByDate(date: String): Maybe<List<JogEntries>> = jogEntriesDAO.getByDate(date)

    fun add(jogEntries: JogEntries): Completable =
        jogEntriesDAO.addUpdateWorkout(jogEntries)

    fun getByJogID(jogId: Int): Maybe<List<JogEntries>> = jogEntriesDAO.getByID(jogId)

    fun deleteAll(): Completable = jogEntriesDAO.deleteAll()
}