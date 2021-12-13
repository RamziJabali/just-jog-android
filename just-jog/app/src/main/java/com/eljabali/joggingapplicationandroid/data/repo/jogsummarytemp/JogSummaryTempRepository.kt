package com.eljabali.joggingapplicationandroid.data.repo.jogsummarytemp

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import java.time.ZonedDateTime

class JogSummaryTempRepository(private val jogSummaryTempDAO: JogSummaryTempDAO) {

    fun add(jogSummaryTemp: JogSummaryTemp): Completable =
        jogSummaryTempDAO.addJogDate(jogSummaryTemp)

    fun deleteAll(): Completable = jogSummaryTempDAO.deleteAll()

    fun getByID(id:Int): Maybe<JogSummaryTemp> = jogSummaryTempDAO.getByID(id)
}