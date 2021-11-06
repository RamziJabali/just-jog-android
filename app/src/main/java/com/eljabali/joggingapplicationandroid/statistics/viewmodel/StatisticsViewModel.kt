package com.eljabali.joggingapplicationandroid.statistics.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.eljabali.joggingapplicationandroid.data.usecase.ModifiedJogDateInformation
import com.eljabali.joggingapplicationandroid.data.usecase.ModifiedJogSummary
import com.eljabali.joggingapplicationandroid.data.usecase.UseCase
import com.eljabali.joggingapplicationandroid.statistics.view.StatisticsViewState
import com.eljabali.joggingapplicationandroid.util.DateFormat
import com.eljabali.joggingapplicationandroid.util.TAG
import com.eljabali.joggingapplicationandroid.util.getTotalDistance
import com.google.android.gms.maps.model.LatLng
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

class StatisticsViewModel(application: Application, private val useCase: UseCase) :
    AndroidViewModel(application) {

    companion object {
        const val NOTHING_JOGGED = "0.00 Miles"
        const val NOTHING_JOGGED_TODAY = "No Entry For Today"
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
        getAllJogsAtSpecificDate(ZonedDateTimes.today.toLocalDate())
    }

    private fun getAllJogsAtSpecificDate(date: LocalDate) {
        useCase.getAllJogsAtSpecificDate(date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { listOfModifiedJogDateInformation ->
                    statisticsViewState = statisticsViewState.copy(
                        todayLastJogDistance = getTodaysRecord(listOfModifiedJogDateInformation)
                    )
                    invalidateView()
                },
                { error -> Log.e(TAG, error.localizedMessage, error) },
            )
            .addTo(compositeDisposable)
    }

    private fun getJogSummariesBetweenTwoDates(startDate: LocalDate, endDate: LocalDate) {
        useCase.getGetJogSummariesBetweenDates(startDate, endDate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { listOfModifiedJogDateInformation ->
                    statisticsViewState = statisticsViewState.copy(
                        weeklyAverageDistance = getWeeklyAverage(listOfModifiedJogDateInformation)
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

    fun deleteAll() {
        useCase.deleteAllEntries()
        statisticsViewState = statisticsViewState.copy(weeklyAverageDistance = NOTHING_JOGGED, todayLastJogDistance = NOTHING_JOGGED_TODAY)
        invalidateView()
    }

    private fun getWeeklyAverage(listOfModifiedJogSummaries: List<ModifiedJogSummary>): String {
        var totalWeeklyMiles = 0.0
        listOfModifiedJogSummaries.forEach { jogSummary ->
           totalWeeklyMiles += jogSummary.totalDistance
        }
        return String.format("%.2f", totalWeeklyMiles / 7.0) + " Miles"
    }


    private fun getTodaysRecord(listOfModifiedJogDates: List<ModifiedJogDateInformation>): String {
        return if (listOfModifiedJogDates.isNotEmpty()) {
            getTotalDistance(
                getListOfLatLangFromModifiedJogDates(listOfModifiedJogDates)
            ).toString() + "Miles"
        } else {
            "No Entry For Today"
        }
    }

    private fun getListOfLatLangFromModifiedJogDates(listOfModifiedJogDateInformation: List<ModifiedJogDateInformation>): List<LatLng> {
        val listOfLatLng: MutableList<LatLng> = mutableListOf()
        for (element in listOfModifiedJogDateInformation) {
            listOfLatLng.add(element.latitudeLongitude)
        }
        return listOfLatLng
    }

    private fun invalidateView() {
        observableStatisticsViewState.onNext(statisticsViewState)
    }

}
