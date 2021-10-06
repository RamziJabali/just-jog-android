package com.eljabali.joggingapplicationandroid.statisticsview


data class StatisticsViewState(
    val time: String = "",
    val date: String= "",
    val distance: Double = 0.0,
    val weeklyAverage: String = "",
    val dailyRecord: String = ""
)