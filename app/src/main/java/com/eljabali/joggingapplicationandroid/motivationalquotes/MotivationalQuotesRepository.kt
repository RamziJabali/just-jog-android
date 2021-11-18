package com.eljabali.joggingapplicationandroid.motivationalquotes
import io.reactivex.Observable
class MotivationalQuotesRepository(private val motivationalQuotesAPI: MotivationalQuotesAPI) {
    fun getAllMotivationalQuotes(): Observable<List<MotivationalQuotes>> =
        motivationalQuotesAPI.getQuote()


}