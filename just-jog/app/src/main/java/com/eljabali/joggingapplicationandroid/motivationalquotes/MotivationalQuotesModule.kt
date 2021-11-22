package com.eljabali.joggingapplicationandroid.motivationalquotes

import org.koin.dsl.module

val motivationalQuotesModule = module {
    single <MotivationalQuotesAPIRepository> { MotivationalQuotesAPIRepository(get<MotivationalQuotesAPI>())}
}