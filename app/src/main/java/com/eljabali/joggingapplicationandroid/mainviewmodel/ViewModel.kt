package com.eljabali.joggingapplicationandroid.mainviewmodel

import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.eljabali.joggingapplicationandroid.mainview.ColoredDates
import com.eljabali.joggingapplicationandroid.mainview.ViewState
import com.eljabali.joggingapplicationandroid.usecase.ModifiedJogDateInformation
import com.eljabali.joggingapplicationandroid.usecase.UseCase
import com.google.android.gms.maps.model.LatLng
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.*

class ViewModel(application: Application, private val useCase: UseCase) :
    AndroidViewModel(application) {

    companion object {
        const val VM_TAG = "ViewModel"
        val hasWorkedOutColor: ColorDrawable =
            ColorDrawable(Color.GREEN)
        val hasNotWorkedOutColor: ColorDrawable =
            ColorDrawable(Color.RED)
        val noJogRecorded: ColorDrawable =
            ColorDrawable(Color.WHITE)
    }

    private var viewState = ViewState()
    private var compositeDisposable = CompositeDisposable()

    val viewStateObservable = BehaviorSubject.create<ViewState>()


    fun getAllEntries() {
        compositeDisposable.add(
            useCase.getAllJogs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { listOfModifiedJogInformation ->
                        viewState =
                            viewState.copy(
                                listOfModifiedDates = listOfModifiedJogInformation,
                                listOfDates = getDatesFromModifiedJogInformation(
                                    listOfModifiedJogInformation
                                )
                            )
                        invalidateView()
                    },
                    { error -> Log.e(VM_TAG, error.localizedMessage, error) }
                ))
    }

    private fun getDatesFromModifiedJogInformation(listOfModifiedJogDateInformation: List<ModifiedJogDateInformation>): List<ColoredDates> {
        val listOfDates: MutableList<ColoredDates> = mutableListOf()
        if (listOfModifiedJogDateInformation.isNotEmpty()) {
            var tempDate: Date = Date.from(listOfModifiedJogDateInformation[0].dateTime.toInstant())
            listOfDates.add(ColoredDates(tempDate, hasWorkedOutColor))
            for (element in listOfModifiedJogDateInformation) {
                if (tempDate != Date.from(element.dateTime.toInstant())) {
                    tempDate = Date.from(element.dateTime.toInstant())
                    listOfDates.add(ColoredDates(tempDate, hasWorkedOutColor))
                }
            }
            return listOfDates
        }
        for (element in viewState.listOfDates) {
            listOfDates.add(ColoredDates(element.date, noJogRecorded))
        }
        return listOfDates
    }

    private fun invalidateView() {
        viewStateObservable.onNext(viewState)
    }

    private fun addDatesToCalendar() {

    }

}