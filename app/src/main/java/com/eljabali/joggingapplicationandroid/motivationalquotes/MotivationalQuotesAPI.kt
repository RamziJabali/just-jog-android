package com.eljabali.joggingapplicationandroid.motivationalquotes

import io.reactivex.Observable
import retrofit2.http.GET

interface MotivationalQuotesAPI {
    @GET("quotes/")
    fun getQuote(): Observable<List<MotivationalQuotes>>
}