package com.eljabali.joggingapplicationandroid.map.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eljabali.joggingapplicationandroid.data.usecase.JogUseCase
import com.eljabali.joggingapplicationandroid.data.usecase.ModifiedJogDateInformation
import com.eljabali.joggingapplicationandroid.map.view.MapsViewState
import com.eljabali.joggingapplicationandroid.util.TAG
import com.google.android.gms.maps.model.LatLng
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class MapsViewModel(private val jogUseCase: JogUseCase) : ViewModel() {

    val mapsLiveData: MutableLiveData<MapsViewState> by lazy {
        MutableLiveData<MapsViewState>()
    }
    private var mapsViewState = MapsViewState()
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getAllJogsAtSpecificID(runID: Int) {
        jogUseCase.getJogEntriesById(runID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { listOfSpecificDates ->
                    val pointsOfRun = getJogLatLngPoints(listOfSpecificDates)
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
        specificDate: List<ModifiedJogDateInformation>
    ): List<LatLng> {
        val listOfLatLng = mutableListOf<LatLng>()
        specificDate.forEach { date ->
            listOfLatLng.add(date.latitudeLongitude)
        }
        return listOfLatLng
    }

    private fun invalidateView() {
        mapsLiveData.value = mapsViewState
    }

}