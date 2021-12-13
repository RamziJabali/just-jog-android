package com.eljabali.joggingapplicationandroid.koin

import com.eljabali.joggingapplicationandroid.data.repo.JogDatabase
import com.eljabali.joggingapplicationandroid.data.repo.jogentries.JogEntriesRepository
import com.eljabali.joggingapplicationandroid.data.repo.jogsummary.JogSummaryRepository
import com.eljabali.joggingapplicationandroid.data.repo.jogsummarytemp.JogSummaryTempRepository
import com.eljabali.joggingapplicationandroid.data.usecase.JogUseCase
import com.eljabali.joggingapplicationandroid.home.HomeViewModel
import com.eljabali.joggingapplicationandroid.map.viewmodel.MapsViewModel
import com.eljabali.joggingapplicationandroid.motivationalquotes.MotivationalQuotesAPIRepository
import com.eljabali.joggingapplicationandroid.statistics.viewmodel.StatisticsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val calendarModule = module {
    viewModel { HomeViewModel(androidApplication(), get<JogUseCase>()) }
    single {
        JogUseCase(
            get<JogEntriesRepository>(),
            get<JogSummaryRepository>(),
            get<JogSummaryTempRepository>(),
            get<MotivationalQuotesAPIRepository>(),
        )
    }
    single { JogEntriesRepository(get<JogDatabase>().jogDAO()) }
    single { JogSummaryRepository(get<JogDatabase>().jogSummaryDAO()) }
    single { JogSummaryTempRepository(get<JogDatabase>().jogSummaryTempDAO()) }
    single { JogDatabase.getInstance(androidApplication().applicationContext) }
}
val statisticsModule = module {
    viewModel { StatisticsViewModel(androidApplication(), get<JogUseCase>()) }
}
val mapsModule = module {
    viewModel { MapsViewModel(get<JogUseCase>()) }
}



