package com.eljabali.joggingapplicationandroid.statisticsview

import com.eljabali.joggingapplicationandroid.usecase.ModifiedJogDateInformation

data class StatisticsViewState(
    val time: String = "",
    val date: String= "",
    val distance: Double = 0.0,
    val listOfModifiedJogDateInformation: List<ModifiedJogDateInformation> = emptyList()
)