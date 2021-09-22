package com.eljabali.joggingapplicationandroid.statisticsview

import com.eljabali.joggingapplicationandroid.usecase.ModifiedJogDateInformation

data class StatisticsViewState(
    val time: String = "",
    val date: String= "",
    var distance: Double = 0.0,
    var listOfModifiedJogDateInformation: List<ModifiedJogDateInformation> = emptyList()
)