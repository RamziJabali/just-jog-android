package com.eljabali.joggingapplicationandroid.koin

import com.eljabali.joggingapplicationandroid.data.repo.WorkoutDatabase
import com.eljabali.joggingapplicationandroid.data.repo.WorkoutRepository
import com.eljabali.joggingapplicationandroid.statistics.viewmodel.StatisticsViewModel
import com.eljabali.joggingapplicationandroid.data.usecase.UseCase
import com.eljabali.joggingapplicationandroid.calendar.mainviewmodel.ViewModel
import com.eljabali.joggingapplicationandroid.map.viewmodel.MapsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val calendarModule = module {
    viewModel { ViewModel(androidApplication(), get<UseCase>()) }
    single { UseCase(get<WorkoutRepository>()) }
    single { WorkoutRepository(get<WorkoutDatabase>().workoutDAO()) }
    single { WorkoutDatabase.getInstance(androidApplication().applicationContext) }
}

val statisticsModule = module {
    viewModel { StatisticsViewModel(androidApplication(), get<UseCase>()) }
}
val mapsModule = module {
    viewModel { MapsViewModel(get<UseCase>()) }
}



