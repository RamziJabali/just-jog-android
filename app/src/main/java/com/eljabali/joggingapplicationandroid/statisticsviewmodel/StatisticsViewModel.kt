package com.eljabali.joggingapplicationandroid.statisticsviewmodel

import androidx.lifecycle.ViewModel
import com.eljabali.joggingapplicationandroid.statisticsview.StatisticsViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import zoneddatetime.ZonedDateTimes
import zoneddatetime.extensions.print
import java.util.concurrent.TimeUnit

class StatisticsViewModel : ViewModel() {

    val observableStatisticsViewState = BehaviorSubject.create<StatisticsViewState>()

    private var statisticsViewState = StatisticsViewState()
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun onFragmentLaunch() {
        compositeDisposable.add(Observable.interval(1, TimeUnit.SECONDS)
            .timeInterval()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val now = ZonedDateTimes.now
                statisticsViewState = statisticsViewState.copy(
                    time = now.print("HH:mm:ss"),
                    date = now.print("EEE, MMM d yyyy")
                )
                invalidateView()
            })

    }

    private fun invalidateView() {
        observableStatisticsViewState.onNext(statisticsViewState)
    }
}