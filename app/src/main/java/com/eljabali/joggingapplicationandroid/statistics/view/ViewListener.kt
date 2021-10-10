package com.eljabali.joggingapplicationandroid.statistics.view

interface ViewListener {
    fun setNewViewState(statisticsViewState: StatisticsViewState)
    fun monitorStatisticsViewState()
}