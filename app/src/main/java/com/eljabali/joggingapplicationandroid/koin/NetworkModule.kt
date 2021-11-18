package com.eljabali.joggingapplicationandroid.koin

import com.eljabali.joggingapplicationandroid.motivationalquotes.MotivationalQuotesAPI
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "https://type.fit/api/"

val networkModule = module {
    single<MotivationalQuotesAPI> { get<Retrofit>().create(MotivationalQuotesAPI::class.java) }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(get<GsonConverterFactory>())
            .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
            .build()
    }

    single<OkHttpClient> {
        OkHttpClient.Builder().build()
    }

    single<GsonConverterFactory> {
        GsonConverterFactory.create()
    }

    single<RxJava2CallAdapterFactory> {
        RxJava2CallAdapterFactory.create()
    }
}