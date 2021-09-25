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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.time.ZonedDateTime
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
                                listOfColoredDates = getDatesFromModifiedJogInformation(
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
            var tempDate = listOfModifiedJogDateInformation[0].dateTime
            listOfDates.add(ColoredDates(getDateFromZonedDateTime(tempDate), hasWorkedOutColor))
            for (element in listOfModifiedJogDateInformation) {
                if (tempDate.year != element.dateTime.year || tempDate.dayOfYear != element.dateTime.dayOfYear) {
                    tempDate = element.dateTime
                    listOfDates.add(
                        ColoredDates(
                            getDateFromZonedDateTime(tempDate),
                            hasWorkedOutColor
                        )
                    )
                }
            }
            return listOfDates
        }
        return listOfDates
    }


//    private fun getDatesFromModifiedJogInformation(listOfModifiedJogDateInformation: List<ModifiedJogDateInformation>): List<ColoredDates> {
//        val listOfDates: MutableList<ColoredDates> = mutableListOf()
//        if (listOfModifiedJogDateInformation.isNotEmpty()) {
//            var tempDate = listOfModifiedJogDateInformation[0].dateTime
//            listOfDates.add(ColoredDates(getDateFromZonedDateTime(tempDate), hasWorkedOutColor))
//            for (element in listOfModifiedJogDateInformation) {
//                if (tempDate.year != element.dateTime.year || tempDate.dayOfYear != element.dateTime.dayOfYear) {
//                    tempDate = element.dateTime
//                    listOfDates.add(
//                        ColoredDates(
//                            getDateFromZonedDateTime(tempDate),
//                            hasWorkedOutColor
//                        )
//                    )
//                }
//            }
//            return listOfDates
//        }
//        return listOfDates
//    }

    private fun getDateFromZonedDateTime(dateTime: ZonedDateTime): Date =
        Date.from(dateTime.toInstant())


    private fun invalidateView() {
        viewStateObservable.onNext(viewState)
    }

    private fun addDatesToCalendar() {

    }

}