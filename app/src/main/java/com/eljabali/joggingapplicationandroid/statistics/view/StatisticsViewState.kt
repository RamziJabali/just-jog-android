package com.eljabali.joggingapplicationandroid.statistics.view

data class StatisticsViewState(
    val timeNow: String = "",
    val dateToday: String = "",
    val weeklyAverageDistance: String = "",
    val todayLastJogDistance: String = "",
    val isUserJogging: Boolean = false
)