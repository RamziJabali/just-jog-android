package com.eljabali.joggingapplicationandroid.motivationalquotes

import io.reactivex.Observable

class MotivationalQuotesAPIRepository(
    private val motivationalQuotesAPI: MotivationalQuotesAPI
) {

    fun getMotivationalQuote(): Observable<MotivationalQuotes> = motivationalQuotesAPI.getQuote()

}