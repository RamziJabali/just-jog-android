package com.eljabali.joggingapplicationandroid.statisticsviewmodel

import android.annotation.SuppressLint
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

    fun getAverageMillagePerWeek(){
        var latLng6 = LatLng(33.37856700076846, -112.13381645952278) //start point
        var latLng7 = LatLng(33.39223722036783, -112.13348390000411) //end point
        statisticsViewState.distance = getDistanceBetweenTwoLatLngToMiles(latLng6,latLng7)
        invalidateView()
    }

    @SuppressLint("CheckResult")
    fun getAllJogs() {
        useCase.getAllJogs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { listOfModifiedJogDates ->
                    statisticsViewState.listOfModifiedJogDateInformation = listOfModifiedJogDates
                    invalidateView()
                },
                { error -> Log.e(SVM_TAG, error.localizedMessage, error) })
    }

    fun deleteAll() {
        useCase.deleteAllEntries()
        invalidateView()
    }

    fun addJog() {
        val latLng = LatLng(33.380914, -112.133667)
        val latLng2 = LatLng(33.380999, -112.133662)
        val latLng3 = LatLng(33.381283, -112.133662)
        val latLng4 = LatLng(33.381656, -112.133665)
        val latLng5 = LatLng(33.383449, -112.133673)

        useCase.addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now(),
                1,
                latLng
            )
        )
        Thread.sleep(1_000)
        useCase.addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now(),
                1,
                latLng2
            )
        )
        Thread.sleep(1_000)
        useCase.addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now(),
                1,
                latLng3
            )
        )
        Thread.sleep(1_000)
        useCase.addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now(),
                1,
                latLng4
            )
        )
        Thread.sleep(1_000)
        useCase.addJog(
            modifiedJogDateInformation = ModifiedJogDateInformation(
                dateTime = ZonedDateTime.now(),
                1,
                latLng5
            )
        )
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
        val rounded = String.format("%.3f", final)
        return rounded.toDouble()
    }

    private fun degreesToRadian(degree: Double): Double {
        return (degree * (PI / 180))
    }

    private fun invalidateView() {
        observableStatisticsViewState.onNext(statisticsViewState)
    }
}
