package com.eljabali.joggingapplicationandroid.statisticsviewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
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
import java.lang.Math.*
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit
import kotlin.math.cos
import kotlin.math.sin

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
        getAllJogs()
    }

    private fun setUpClock() {
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

    private fun getAllJogs() {
        compositeDisposable.add(
            useCase.getAllJogs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { listOfModifiedJogDates ->
                        statisticsViewState = statisticsViewState.copy(
                            listOfModifiedJogDateInformation = listOfModifiedJogDates,
                            distance = getTotalDistance(
                                getListOfLatLangFromModifiedJogDates(
                                    listOfModifiedJogDates
                                )
                            )
                        )
                        invalidateView()
                    },
                    { error -> Log.e(SVM_TAG, error.localizedMessage, error) })
        )
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
                dateTime = ZonedDateTime.now().minusDays(7),
                1,
                latLng8
            )
        )
        Thread.sleep(1000)
        addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now().minusDays(5),
                1,
                latLng7
            )
        )
        Thread.sleep(1000)
        addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now().minusDays(4),
                1,
                latLng6
            )
        )
        Thread.sleep(1000)
        addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now().minusDays(3),
                1,
                latLng5
            )
        )

        Thread.sleep(1000)
        addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now().minusDays(2),
                1,
                latLng4
            )
        )
        Thread.sleep(1000)
        addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now().minusDays(1),
                1,
                latLng3
            )
        )
        Thread.sleep(1000)
        addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now(),
                1,
                latLng2
            )
        )
        Thread.sleep(1000)
        addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now(),
                1,
                latLng
            )
        )
    }

    private fun getTotalDistance(listOfPoints: List<LatLng>): Double {
        var totalDistance = 0.0
        if (listOfPoints.size >= 2) {
            var index = 1
            while (index < listOfPoints.size) {
                totalDistance += getDistanceBetweenTwoLatLngToMiles(
                    listOfPoints[index - 1],
                    listOfPoints[index]
                )
                index++
            }
            return String.format("%.3f", totalDistance).toDouble()
        }
        return totalDistance
    }

    private fun getDistanceBetweenTwoLatLngToMiles(point1: LatLng, point2: LatLng): Double {
        val earthsRadius = 3958.8
        val dLat = degreesToRadian(point1.latitude - point2.latitude)
        val dLon = degreesToRadian(point1.longitude - point2.longitude)
        val a =
            sin(dLat / 2) * sin(dLat / 2) +
                    cos(degreesToRadian(point1.latitude)) * cos(degreesToRadian(point2.latitude)) *
                    sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
        val final = earthsRadius * c

        return String.format("%.3f", final).toDouble()
    }

    private fun getListOfLatLangFromModifiedJogDates(listOfModifiedJogDateInformation: List<ModifiedJogDateInformation>): List<LatLng> {
        val listOfLatLng: MutableList<LatLng> = mutableListOf()
        for (element in listOfModifiedJogDateInformation) {
            listOfLatLng.add(element.latitudeLongitude)
        }
        return listOfLatLng
    }

    private fun degreesToRadian(degree: Double): Double {
        return (degree * (PI / 180))
    }

    private fun invalidateView() {
        observableStatisticsViewState.onNext(statisticsViewState)
    }
}
