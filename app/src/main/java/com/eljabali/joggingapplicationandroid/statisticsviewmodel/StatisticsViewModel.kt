package com.eljabali.joggingapplicationandroid.statisticsviewmodel

import androidx.lifecycle.ViewModel
import com.eljabali.joggingapplicationandroid.statisticsview.StatisticsViewState
import io.reactivex.subjects.BehaviorSubject
import zoneddatetime.ZonedDateTimes
import zoneddatetime.extensions.print

class StatisticsViewModel : ViewModel() {

    private var statisticsViewState = StatisticsViewState()

    val observableStatisticsViewState = BehaviorSubject.create<StatisticsViewState>()

    fun onFragmentLaunch() {
        val now = ZonedDateTimes.now
        statisticsViewState = statisticsViewState.copy(
            time = now.print("HH:mm"),
            date = now.print("EEE, MMM d yyyy")
        )
        invalidateView()
    }

    private fun invalidateView() {
        observableStatisticsViewState.onNext(statisticsViewState)
    }
}