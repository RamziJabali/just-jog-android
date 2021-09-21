package com.eljabali.joggingapplicationandroid.statisticsviewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
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
import java.time.ZonedDateTime
import java.util.*
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
        compositeDisposable.add(Observable.interval(1, TimeUnit.SECONDS)
            .timeInterval()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val now = ZonedDateTimes.now
                statisticsViewState = statisticsViewState.copy(
                    time = now.print("HH:mm:ss"),
                    date = now.print("EEE, MMM d yyyy"))
                invalidateView()
            })
    }

    @SuppressLint("CheckResult")
    fun getAllJogs() {
        useCase.getAllJogs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { listOfModifiedJogDates ->
                    Log.i(SVM_TAG,listOfModifiedJogDates[0].dateTime.toString())
                    invalidateView()
                },
                { error -> Log.e(SVM_TAG, error.localizedMessage, error) })
        invalidateView()
    }

    fun deleteAll() {
        useCase.deleteAllEntries()
        invalidateView()
    }

    fun addJog() {
        var latLng = LatLng(33.380914, -112.133667)
        var latLng2 = LatLng(33.380999, -112.133662)
        var latLng3 = LatLng(33.381283, -112.133662)
        var latLng4 = LatLng(33.381656, -112.133665)
        var latLng5 = LatLng(33.383449, -112.133673)


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

    private fun invalidateView() {
        observableStatisticsViewState.onNext(statisticsViewState)
    }
}
