package com.eljabali.joggingapplicationandroid.motivationalquotes

import org.koin.dsl.module

val motivationalQuotesModule = module {
    single <MotivationalQuotesRepository> { MotivationalQuotesRepository(get<MotivationalQuotesAPI>())}
}