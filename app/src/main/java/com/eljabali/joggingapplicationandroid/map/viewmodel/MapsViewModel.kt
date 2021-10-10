package com.eljabali.joggingapplicationandroid.map.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.eljabali.joggingapplicationandroid.data.usecase.ModifiedJogDateInformation
import com.eljabali.joggingapplicationandroid.data.usecase.UseCase
import com.google.android.gms.maps.model.LatLng
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.time.LocalDate

class MapsViewModel(private val useCase: UseCase) : ViewModel() {

    companion object {
        const val MVM_TAG = "MapsViewModel"
    }

    private var mapsViewState = MapsViewState()
    private val compositeDisposable = CompositeDisposable()

    val mapsViewStateObservable = BehaviorSubject.create<MapsViewState>()

    fun getAllJogsAtSpecificDate(localDate: LocalDate, runID: Int) {
        compositeDisposable.add(
            useCase.getAllJogsAtSpecificDate(localDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { listOfSpecificDates ->
                        mapsViewState = mapsViewState.copy(
                            listOfLatLng = getSpecificJogListOfLatLng(
                                runID,
                                listOfSpecificDates
                            )
                        )
                        invalidateView()
                    },
                    { error -> Log.e(MVM_TAG, error.localizedMessage, error) }
                )
        )
    }

    private fun getSpecificJogListOfLatLng(
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}