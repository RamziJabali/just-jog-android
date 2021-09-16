package com.eljabali.joggingapplicationandroid.statisticsviewmodel

import androidx.lifecycle.ViewModel
import com.eljabali.joggingapplicationandroid.statisticsview.StatisticsViewState
import io.reactivex.subjects.BehaviorSubject
import zoneddatetime.ZonedDateTimes

class StatisticsViewModel : ViewModel() {

    private var statisticsViewState = StatisticsViewState()

    val observableStatisticsViewState = BehaviorSubject.create<StatisticsViewState>()

    fun onFragmentLaunch() {
        val zoneddatetime = ZonedDateTimes.now
        statisticsViewState = statisticsViewState.copy(
            time = "${zoneddatetime.hour}:${zoneddatetime.minute}:${zoneddatetime.second} ",
            date = "${zoneddatetime.month}/${zoneddatetime.dayOfMonth}/${zoneddatetime.year}"
        )
        invalidateView()
    }

    private fun invalidateView() {
        observableStatisticsViewState.onNext(statisticsViewState)
    }
}