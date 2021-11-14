package com.eljabali.joggingapplicationandroid.statistics.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.eljabali.joggingapplicationandroid.data.usecase.JogUseCase
import com.eljabali.joggingapplicationandroid.data.usecase.ModifiedJogSummary
import com.eljabali.joggingapplicationandroid.statistics.view.StatisticsViewState
import com.eljabali.joggingapplicationandroid.statistics.view.WeeklyStats
import com.eljabali.joggingapplicationandroid.util.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import localdate.LocalDates
import zoneddatetime.ZonedDateTimes
import zoneddatetime.extensions.print
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class StatisticsViewModel(application: Application, private val jogUseCase: JogUseCase) :
    AndroidViewModel(application) {

    companion object {
        const val NOTHING_JOGGED_TODAY = "Be your own change!"
        const val MILES = "Miles"
        const val RUNS = "Runs"
        const val YOU_RAN_TODAY = "You ran today!"
        const val FOR = "For"
        const val MINS_AT = "mins at"
        const val MPH = "MPH!"
    }

    val observableStatisticsViewState = BehaviorSubject.create<StatisticsViewState>()
    private var statisticsViewState = StatisticsViewState()
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun onFragmentLaunch() {
        setUpClock()
        getJogSummariesBetweenTwoDates(
            LocalDates.lastMonday,
            LocalDates.today
        )
        getJogSummariesAtDate(ZonedDateTimes.today.toLocalDate())
    }

    private fun getJogSummariesAtDate(date: LocalDate) {
        jogUseCase.getJogSummariesAtDate(date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { listOfJogSummaries ->
                    statisticsViewState = statisticsViewState.copy(
                        todayLastJogDistance = getLastJog(listOfJogSummaries)
                    )
                    invalidateView()
                },
                { error -> Log.e(TAG, error.localizedMessage, error) },
            )
            .addTo(compositeDisposable)
    }

    private fun getLastJog(listOfJogSummaries: List<ModifiedJogSummary>): String =
        if (listOfJogSummaries.isEmpty()) NOTHING_JOGGED_TODAY
        else {
            with(listOfJogSummaries) {
                val mph = getMPH(get(size - 1).totalDistance, get(size - 1).timeDurationInSeconds)
                val totalTimeInMinutes = get(size - 1).timeDurationInSeconds / 60
                "${YOU_RAN_TODAY}\n $FOR $totalTimeInMinutes $MINS_AT $mph $MPH"
            }
        }

    private fun getJogSummariesBetweenTwoDates(startDate: LocalDate, endDate: LocalDate) {
        jogUseCase.getGetJogSummariesBetweenDates(startDate, endDate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { listOfModifiedJogDateInformation ->
                    statisticsViewState = statisticsViewState.copy(
                        weeklyStats = getWeeklyStats(listOfModifiedJogDateInformation)
                    )
                    invalidateView()
                },
                { error -> Log.e(TAG, error.localizedMessage, error) })
            .addTo(compositeDisposable)

    }

    private fun setUpClock() {
        Observable.interval(1, TimeUnit.SECONDS)
            .timeInterval()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val now = ZonedDateTimes.now
                statisticsViewState = statisticsViewState.copy(
                    timeNow = now.print(DateFormat.HH_MM_SS.format),
                    dateToday = now.print(DateFormat.EEE_MMM_D_YYYY.format)
                )
                invalidateView()
            }
            .addTo(compositeDisposable)
    }

    private fun getWeeklyStats(listOfModifiedJogSummaries: List<ModifiedJogSummary>): WeeklyStats {
        var totalWeeklyMiles = 0.0
        var totalWeeklyRuns = 0
        var totalWeeklyJogTime: Long = 0
        listOfModifiedJogSummaries.forEach { jogSummary ->
            totalWeeklyMiles += jogSummary.totalDistance
            totalWeeklyJogTime += jogSummary.timeDurationInSeconds
            totalWeeklyRuns++
        }
        val averageDistance = String.format("%.2f", totalWeeklyMiles / 7.0) + " $MILES"
        val averageRuns = "${totalWeeklyRuns / 7} $RUNS"
        val averageTime: String = getFormattedTime(totalWeeklyJogTime / 7, DurationFormat.H_M_S)
        return WeeklyStats(
            WeeklyStats.WeeklyAverageStats(averageDistance, averageRuns, averageTime),
            WeeklyStats.WeeklyTotalStats(
                "- " + String.format("%.2f", totalWeeklyMiles) + " $MILES",
                "- $totalWeeklyRuns $RUNS",
                "- " + getFormattedTime(totalWeeklyJogTime, DurationFormat.H_M_S)
            )
        )
    }

    private fun invalidateView() {
        observableStatisticsViewState.onNext(statisticsViewState)
    }

}
