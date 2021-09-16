package com.eljabali.joggingapplicationandroid.statisticsview

interface ViewListener {
    fun setNewViewState(statisticsViewState: StatisticsViewState)
    fun monitorStatisticsViewState()
}