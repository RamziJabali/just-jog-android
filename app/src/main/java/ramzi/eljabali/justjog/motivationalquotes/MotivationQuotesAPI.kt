package ramzi.eljabali.justjog.motivationalquotes

import retrofit2.Response
import retrofit2.http.GET

interface MotivationQuotesAPI {
    @GET("/quotes/random?minLength=100&maxLength=140&tags=Motivational/")
    suspend fun getQuote(): Response<Array<MotivationalQuote>>
}