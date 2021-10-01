package com.eljabali.joggingapplicationandroid.mainviewmodel

import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.eljabali.joggingapplicationandroid.libraries.DateFormat
import com.eljabali.joggingapplicationandroid.libraries.getFormattedTime
import com.eljabali.joggingapplicationandroid.libraries.getTotalDistance
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
import zoneddatetime.extensions.print
import java.time.Duration
import java.time.ZoneId
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
        val listOfTime = mutableListOf<ZonedDateTime>()
        var currentEntry = 1
        var index = 0
        listOfModifiedJogDateInformation.forEach { modifiedJogDateInformation ->
            listOfLatLng.add(modifiedJogDateInformation.latitudeLongitude)
            listOfTime.add(modifiedJogDateInformation.dateTime)
            if (currentEntry != modifiedJogDateInformation.runNumber || index == listOfModifiedJogDateInformation.size - 1) {
                listOfRecyclerViewProperties.add(
                    RecyclerViewProperties(
                        totalDistance = getTotalDistance(listOfLatLng).toString(),
                        totalTime = getFormattedTime(
                            Duration.between(
                                listOfTime[0],
                                listOfTime[listOfTime.size - 1]
                            ).seconds
                        ),
                        jogEntry = currentEntry.toString(),
                        milesPerHour = "",
                        date = modifiedJogDateInformation.dateTime.print(DateFormat.YYYY_MM_DD.format)
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
            var tempDate = listOfModifiedJogDateInformation[0].dateTime
            listOfDates.add(ColoredDates(convertZonedDateTimeToDate(tempDate), hasWorkedOutColor))
            listOfZonedDateTime.add(tempDate)
            for (element in listOfModifiedJogDateInformation) {
                if (tempDate.year != element.dateTime.year || tempDate.dayOfYear != element.dateTime.dayOfYear) {
                    tempDate = element.dateTime
                    listOfDates.add(
                        ColoredDates(
                            convertZonedDateTimeToDate(tempDate),
                            hasWorkedOutColor
                        )
                    )
                    listOfZonedDateTime.add(tempDate)
                }
            }
            tempDate = listOfModifiedJogDateInformation[0].dateTime
            var index = 0
            while (tempDate.year != ZonedDateTime.now().year || tempDate.dayOfYear != ZonedDateTime.now().dayOfYear) {
                if (index >= listOfZonedDateTime.size ||tempDate.year != listOfZonedDateTime[index].year || tempDate.dayOfYear != listOfZonedDateTime[index].dayOfYear) {
                    listOfDates.add(
                        ColoredDates(
                            convertZonedDateTimeToDate(tempDate),
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


    private fun convertZonedDateTimeToDate(dateTime: ZonedDateTime): Date =
        Date.from(dateTime.toInstant())

    private fun invalidateView() {
        viewStateObservable.onNext(viewState)
    }
}