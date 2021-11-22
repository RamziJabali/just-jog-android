package com.eljabali.joggingapplicationandroid.map.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.eljabali.joggingapplicationandroid.data.usecase.JogUseCase
import com.eljabali.joggingapplicationandroid.data.usecase.ModifiedJogDateInformation
import com.eljabali.joggingapplicationandroid.util.TAG
import com.google.android.gms.maps.model.LatLng
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.time.LocalDate

class MapsViewModel(private val jogUseCase: JogUseCase) : ViewModel() {

    val mapsViewStateObservable = BehaviorSubject.create<MapsViewState>()

    private var mapsViewState = MapsViewState()
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getAllJogsAtSpecificDate(localDate: LocalDate, runID: Int) {
        jogUseCase.getAllJogsAtSpecificDate(localDate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { listOfSpecificDates ->
                    val pointsOfRun = getJogLatLngPoints(runID, listOfSpecificDates)
                    mapsViewState = mapsViewState.copy(
                        listOfLatLng = pointsOfRun,
                    )
                    invalidateView()
                },
                { error -> Log.e(TAG, error.localizedMessage, error) }
            )
            .addTo(compositeDisposable)
    }

    private fun getJogLatLngPoints(
        jogID: Int,
        specificDate: List<ModifiedJogDateInformation>
    ): List<LatLng> {
        val listOfLatLng = mutableListOf<LatLng>()
        specificDate.forEach { date ->
            if (date.runNumber == jogID) {
                listOfLatLng.add(date.latitudeLongitude)
            }
        }
        return listOfLatLng
    }

    private fun invalidateView() {
        mapsViewStateObservable.onNext(mapsViewState)
    }

}