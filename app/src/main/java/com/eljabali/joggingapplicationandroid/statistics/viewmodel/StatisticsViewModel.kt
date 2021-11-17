package com.eljabali.joggingapplicationandroid.statistics.viewmodel

import android.app.Application
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.eljabali.joggingapplicationandroid.JoggingApplication
import com.eljabali.joggingapplicationandroid.R
import com.eljabali.joggingapplicationandroid.data.usecase.JogUseCase
import com.eljabali.joggingapplicationandroid.data.usecase.ModifiedJogSummary
import com.eljabali.joggingapplicationandroid.statistics.view.StatisticsViewState
import com.eljabali.joggingapplicationandroid.statistics.view.WeeklyStats
import com.eljabali.joggingapplicationandroid.util.*
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import zoneddatetime.ZonedDateTimes
import zoneddatetime.extensions.isEqualDay
import zoneddatetime.extensions.print
import java.time.LocalDate
import java.time.ZonedDateTime

class StatisticsViewModel(application: Application, private val jogUseCase: JogUseCase) :
        AndroidViewModel(application) {

    val observableStatisticsViewState = BehaviorSubject.create<StatisticsViewState>()
    private var statisticsViewState = StatisticsViewState()
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun onFragmentLaunch() {
        statisticsViewState =
                statisticsViewState.copy(dateToday = ZonedDateTimes.now.print(DateFormat.EEE_MMM_D_YYYY.format))
        getJogSummariesBetweenTwoDates(
                ZonedDateTimes.lastMonday,
                ZonedDateTimes.today
        )
        getJogSummariesAtDate(ZonedDateTimes.today.toLocalDate())
    }

    private fun getBarChartStats(listOfJogs: List<ModifiedJogSummary>): BarData {
        val entries = mutableListOf<BarEntry>()
        val weeksSummaries = mutableListOf<Double>()
        var tempDate = ZonedDateTimes.lastMonday
        val endDate = tempDate.plusDays(7)
        var index = 0
        var totalDistancePerDay = 0.0
        while (!tempDate.isEqual(endDate)) {
            if (index < listOfJogs.size && tempDate.isEqualDay(listOfJogs[index].date)) {
                if (index + 1 < listOfJogs.size && !listOfJogs[index].date.isEqualDay(listOfJogs[index + 1].date) || index + 1 >= listOfJogs.size) {
                    totalDistancePerDay += listOfJogs[index].totalDistance
                    weeksSummaries.add(totalDistancePerDay)
                    tempDate = tempDate.plusDays(1)
                    totalDistancePerDay = 0.0
                } else {
                    totalDistancePerDay += listOfJogs[index].totalDistance
                }
                index++
            } else {
                tempDate = tempDate.plusDays(1)
                weeksSummaries.add(0.0)
            }
        }
        for (dayOfTheWeek in 0..6) {
            entries.add(BarEntry(dayOfTheWeek.toFloat(), weeksSummaries[dayOfTheWeek].toFloat()))
        }
        val barDataSet = BarDataSet(entries, "Distance(Miles)").apply {
            color = Color.WHITE
        }
        return BarData(barDataSet).apply {
            setValueTextColor(Color.BLUE)
            setValueTextSize(12f)
            barWidth = 0.8f
        }
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
            if (listOfJogSummaries.isEmpty()) getApplication<JoggingApplication>().getString(R.string.be_your_own_change)
            else {
                val timeMinutes =
                        listOfJogSummaries[listOfJogSummaries.size - 1].timeDurationInSeconds / 60
                val mph = getMPH(
                        listOfJogSummaries[listOfJogSummaries.size - 1].totalDistance,
                        listOfJogSummaries[listOfJogSummaries.size - 1].timeDurationInSeconds
                )
                val application = getApplication<JoggingApplication>()
                "${application.getString(R.string.you_ran_today)}\n${application.getString(R.string.for_1)} $timeMinutes ${application.getString(R.string.mins_at)} $mph ${application.getString(R.string.mph)}"
            }

    private fun getJogSummariesBetweenTwoDates(startDate: ZonedDateTime, endDate: ZonedDateTime) {
        jogUseCase.getJogSummariesBetweenDates(startDate, endDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { listOfModifiedJogDateInformation ->
                            val weeklyStats = getWeeklyStats(listOfModifiedJogDateInformation)
                            val barData = getBarChartStats(listOfModifiedJogDateInformation)
                            statisticsViewState = statisticsViewState.copy(
                                    weeklyStats = weeklyStats,
                                    barData = barData
                            )
                            invalidateView()
                        },
                        { error -> Log.e(TAG, error.localizedMessage, error) })
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
        val application = getApplication<JoggingApplication>()
        val averageDistance = String.format("%.2f", totalWeeklyMiles / 7.0)+ " " + application.getString(R.string.miles)
        val averageRuns = "${totalWeeklyRuns / 7} ${application.getString(R.string.runs)}"
        val averageTime: String = getFormattedTime(totalWeeklyJogTime / 7, DurationFormat.H_M_S)
        return WeeklyStats(
                WeeklyStats.WeeklyAverageStats(averageDistance, averageRuns, averageTime),
                WeeklyStats.WeeklyTotalStats(
                        String.format("%.2f", totalWeeklyMiles) + " ${application.getString(R.string.miles)}",
                        "$totalWeeklyRuns ${application.getString(R.string.runs)}",
                        getFormattedTime(totalWeeklyJogTime, DurationFormat.H_M_S)
                )
        )
    }

    private fun invalidateView() {
        observableStatisticsViewState.onNext(statisticsViewState)
    }

}
