package com.eljabali.joggingapplicationandroid.mainviewmodel

import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.eljabali.joggingapplicationandroid.libraries.Time
import com.eljabali.joggingapplicationandroid.libraries.getTotalDistance
import com.eljabali.joggingapplicationandroid.libraries.getTotalTime
import com.eljabali.joggingapplicationandroid.mainview.ColoredDates
import com.eljabali.joggingapplicationandroid.mainview.ViewState
import com.eljabali.joggingapplicationandroid.recyclerview.RecyclerViewProperties
import com.eljabali.joggingapplicationandroid.usecase.ModifiedJogDateInformation
import com.eljabali.joggingapplicationandroid.usecase.UseCase
import com.google.android.gms.maps.model.LatLng
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
                .observeOn(Schedulers.io()) //Changed TODO
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { listOfModifiedJogInformation ->
                        viewState =
                            viewState.copy(
                                listOfColoredDates = getDatesFromModifiedJogInformation(
                                    listOfModifiedJogInformation
                                )
                            )
                        invalidateView()
                    },
                    { error -> Log.e(VM_TAG, error.localizedMessage, error) }
                ))
    }

    fun getAllJogsAtSpecificDate(date: Date) {
        compositeDisposable.add(
            useCase.getAllJogsAtSpecificDate(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { listOfSpecificDates ->
                        viewState = viewState.copy(
                            listOfSpecificDates = convertModifiedJoggingListToRecyclerViewProperties(
                                listOfSpecificDates
                            )
                        )
                        invalidateView()
                    },
                    { error -> Log.e(VM_TAG, error.localizedMessage, error) }
                )
        )
    }

    private fun convertModifiedJoggingListToRecyclerViewProperties(listOfModifiedJogDateInformation: List<ModifiedJogDateInformation>): List<RecyclerViewProperties> {
        val listOfRecyclerViewProperties = mutableListOf<RecyclerViewProperties>()
        val listOfLatLng = mutableListOf<LatLng>()
        val listOfTime = mutableListOf<Time>()
        var currentEntry = 1
        var index = 0
        listOfModifiedJogDateInformation.forEach { modifiedJogDateInformation ->
            listOfLatLng.add(modifiedJogDateInformation.latitudeLongitude)
            listOfTime.add(
                Time(
                    modifiedJogDateInformation.hours,
                    modifiedJogDateInformation.minutes,
                    modifiedJogDateInformation.seconds
                )
            )
            if (currentEntry != modifiedJogDateInformation.runNumber || index == listOfModifiedJogDateInformation.size - 1) {
                listOfRecyclerViewProperties.add(
                    RecyclerViewProperties(
                        getTotalDistance(listOfLatLng).toString(),
                        getTotalTime(listOfTime),
                        currentEntry.toString()
                    )
                )

                listOfLatLng.clear()
                listOfTime.clear()
                currentEntry = modifiedJogDateInformation.runNumber
            }
            index++
        }
        return listOfRecyclerViewProperties
    }


    private fun getDatesFromModifiedJogInformation(listOfModifiedJogDateInformation: List<ModifiedJogDateInformation>): List<ColoredDates> {
        val listOfDates: MutableList<ColoredDates> = mutableListOf()
        val listOfZonedDateTime: MutableList<ZonedDateTime> = mutableListOf()
        if (listOfModifiedJogDateInformation.isNotEmpty()) {
            var tempDate = listOfModifiedJogDateInformation[0].date
            listOfDates.add(ColoredDates(getDateFromZonedDateTime(tempDate), hasWorkedOutColor))
            listOfZonedDateTime.add(tempDate)
            for (element in listOfModifiedJogDateInformation) {
                if (tempDate.year != element.date.year || tempDate.dayOfYear != element.date.dayOfYear) {
                    tempDate = element.date
                    listOfDates.add(
                        ColoredDates(
                            getDateFromZonedDateTime(tempDate),
                            hasWorkedOutColor
                        )
                    )
                    listOfZonedDateTime.add(tempDate)
                }
            }
            tempDate = listOfModifiedJogDateInformation[0].date
            var index = 0
            while (tempDate.year != ZonedDateTime.now().year || tempDate.dayOfYear != ZonedDateTime.now().dayOfYear) {
                if (tempDate.year != listOfZonedDateTime[index].year || tempDate.dayOfYear != listOfZonedDateTime[index].dayOfYear) {
                    listOfDates.add(
                        ColoredDates(
                            getDateFromZonedDateTime(tempDate),
                            hasNotWorkedOutColor
                        )
                    )
                    listOfZonedDateTime.add(tempDate)
                } else {
                    index++
                }
                tempDate = tempDate.plusDays(1)
            }
            return listOfDates
        }
        viewState.listOfColoredDates.forEach { coloredDates ->
            listOfDates.add(ColoredDates(coloredDates.date, noJogRecorded))
        }
        return listOfDates
    }

    private fun getDateFromZonedDateTime(dateTime: ZonedDateTime): Date =
        Date.from(dateTime.toInstant())

    private fun invalidateView() {
        viewStateObservable.onNext(viewState)
    }
}