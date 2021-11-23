package com.eljabali.joggingapplicationandroid.statistics.view

import com.github.mikephil.charting.data.BarData

data class StatisticsViewState(
    val timeNow: String = "",
    val dateToday: String = "",
    val weeklyStats: WeeklyStats = WeeklyStats(),
    val barData: BarData = BarData(),
    val youRanToday: String = "",
    val todayLastJogDistance: String = "",
    val isUserJogging: Boolean = false
)

data class WeeklyStats(
    val weeklyAverageStats: WeeklyAverageStats = WeeklyAverageStats(),
    val weeklyTotalStats: WeeklyTotalStats = WeeklyTotalStats()
) {
    data class WeeklyAverageStats(
        val averageDistance: String = "0",
        val averageRuns: String = "0",
        val averageTime: String = "00h 00m 00s"
    )

    data class WeeklyTotalStats(
        val totalDistance: String = "0",
        val totalRuns: String = "0",
        val totalTime: String = "00h 00m 00s"
    )
}
