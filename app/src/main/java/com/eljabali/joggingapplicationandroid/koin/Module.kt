package com.eljabali.joggingapplicationandroid.koin

import com.eljabali.joggingapplicationandroid.repo.WorkoutDatabase
import com.eljabali.joggingapplicationandroid.repo.WorkoutRepository
import com.eljabali.joggingapplicationandroid.statisticsviewmodel.StatisticsViewModel
import com.eljabali.joggingapplicationandroid.usecase.UseCase
import com.eljabali.joggingapplicationandroid.mainviewmodel.ViewModel
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
    viewModel {StatisticsViewModel(androidApplication(), get<UseCase>())}
}