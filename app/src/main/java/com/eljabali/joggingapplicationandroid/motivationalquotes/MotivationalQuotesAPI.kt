package com.eljabali.joggingapplicationandroid.motivationalquotes

import io.reactivex.Observable
import retrofit2.http.GET

interface MotivationalQuotesAPI {
    @GET("random/")
    fun getQuote(): Observable<MotivationalQuotes>
}