package com.eljabali.joggingapplicationandroid.statisticsviewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.eljabali.joggingapplicationandroid.libraries.DateFormat
import com.eljabali.joggingapplicationandroid.libraries.getTotalDistance
import com.eljabali.joggingapplicationandroid.statisticsview.StatisticsViewState
import com.eljabali.joggingapplicationandroid.usecase.ModifiedJogDateInformation
import com.eljabali.joggingapplicationandroid.usecase.UseCase
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import zoneddatetime.ZonedDateTimes
import zoneddatetime.extensions.print
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class StatisticsViewModel(application: Application, private val useCase: UseCase) :
    AndroidViewModel(application) {

    companion object {
        const val SVM_TAG = "Statistics ViewModel"
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
        getAllJogsBetweenTwoDates(
            ZonedDateTimes.today.minusDays(7).toLocalDate(),
            ZonedDateTimes.today.toLocalDate()
        )
        getAllJogsAtSpecificDate(ZonedDateTimes.today.toLocalDate())
    }

    private fun getAllJogsAtSpecificDate(date: LocalDate) {
        compositeDisposable.add(
            useCase.getAllJogsAtSpecificDate(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { listOfModifiedJogDateInformation ->
                        statisticsViewState = statisticsViewState.copy(
                            dailyRecord = getTodaysRecord(listOfModifiedJogDateInformation)
                        )
                        invalidateView()
                    },
                    { error -> Log.e(SVM_TAG, error.localizedMessage, error) },
                )
        )
    }

    private fun getAllJogsBetweenTwoDates(startDate: LocalDate, endDate: LocalDate) {
        compositeDisposable.add(
            useCase.getRangeOfJogsBetweenStartAndEndDate(startDate, endDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { listOfModifiedJogDateInformation ->
                        statisticsViewState = statisticsViewState.copy(
                            weeklyAverage = getWeeklyAverage(listOfModifiedJogDateInformation)
                        )
                        invalidateView()
                    },
                    { error -> Log.e(SVM_TAG, error.localizedMessage, error) })
        )

    }

    private fun setUpClock() {
        compositeDisposable.add(Observable.interval(1, TimeUnit.SECONDS)
            .timeInterval()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val now = ZonedDateTimes.now
                statisticsViewState = statisticsViewState.copy(
                    time = now.print(DateFormat.HH_MM_SS.format),
                    date = now.print(DateFormat.EEE_MMM_D_YYYY.format)
                )
                invalidateView()
            })
    }

    fun deleteAll() {
        useCase.deleteAllEntries()
        invalidateView()
    }

    fun addJog(modifiedJogDateInformation: ModifiedJogDateInformation) {
        compositeDisposable.add(
            useCase.addJog(modifiedJogDateInformation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.i(UseCase.UC_TAG, "Success")
                        invalidateView()
                    },
                    { error -> Log.e(UseCase.UC_TAG, error.localizedMessage, error) })
        )
    }

    fun addFiveJogs() {
        val latLng = LatLng(33.378407552340995, -112.13377654418115)
        val latLng2 = LatLng(33.38074586452286, -112.13377654416735)
        val latLng3 = LatLng(33.38264513696114, -112.13367998464074)
        val latLng4 = LatLng(33.38373809566478, -112.13371217114684)
        val latLng5 = LatLng(33.38512686005226, -112.13364808651048)
        val latLng6 = LatLng(33.38748122592157, -112.13368226534573)
        val latLng7 = LatLng(33.389855392762904, -112.13359635767495)
        val latLng8 = LatLng(33.39222026263584, -112.13362891719325)

        addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now().minusDays(1),
                1,
                latLng8
            )
        )
        Thread.sleep(1000)
        addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now().minusDays(1),
                1,
                latLng7
            )
        )
        Thread.sleep(1000)
        addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now().minusDays(1),
                1,
                latLng6
            )
        )
        Thread.sleep(1000)
        addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now().minusDays(1),
                1,
                latLng5
            )
        )

        Thread.sleep(1000)
        addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now().minusDays(1),
                1,
                latLng4
            )
        )
        Thread.sleep(1000)
        addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now().minusDays(1),
                2,
                latLng3
            )
        )
        Thread.sleep(1000)
        addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now().minusDays(1),

                2,
                latLng2
            )
        )
        Thread.sleep(1000)
        addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now().minusDays(1),

                2,
                latLng
            )
        )
        Thread.sleep(1000)
        addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now().minusDays(1),
                2,
                latLng2
            )
        )
        Thread.sleep(1000)
        addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now().minusDays(1),
                2,
                latLng
            )
        )
    }


    private fun getWeeklyAverage(listOfModifiedJogDateInformation: List<ModifiedJogDateInformation>): String {
        val listOfLatLng: MutableList<LatLng> = mutableListOf()
        var totalWeeklyMiles = 0.0
        var jogNumber = 1
        var index = 0
        listOfModifiedJogDateInformation.forEach { modifiedJogDateInformation ->
            if (jogNumber != modifiedJogDateInformation.runNumber || index == listOfModifiedJogDateInformation.size) {
                totalWeeklyMiles += getTotalDistance(listOfLatLng)
                jogNumber = modifiedJogDateInformation.runNumber
                listOfLatLng.clear()
            } else {
                listOfLatLng.add(modifiedJogDateInformation.latitudeLongitude)
            }
            index++
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
