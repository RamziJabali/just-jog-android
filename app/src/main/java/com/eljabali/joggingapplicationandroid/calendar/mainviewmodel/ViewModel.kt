package com.eljabali.joggingapplicationandroid.calendar.mainviewmodel

import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.eljabali.joggingapplicationandroid.calendar.mainview.ColoredDates
import com.eljabali.joggingapplicationandroid.calendar.mainview.ViewState
import com.eljabali.joggingapplicationandroid.calendar.recyclerview.RecyclerViewProperties
import com.eljabali.joggingapplicationandroid.data.usecase.ModifiedJogDateInformation
import com.eljabali.joggingapplicationandroid.data.usecase.ModifiedJogSummary
import com.eljabali.joggingapplicationandroid.data.usecase.UseCase
import com.eljabali.joggingapplicationandroid.util.*
import com.google.android.gms.maps.model.LatLng
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import zoneddatetime.extensions.print
import java.time.Duration
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

    private val compositeDisposable = CompositeDisposable()
    private var viewState = ViewState()

    val viewStateObservable = BehaviorSubject.create<ViewState>()

    fun getAllDates() {
        useCase.getAllJogSummaries()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { allJogSummaries ->
                    viewState = viewState.copy(
                        listOfColoredDates = getColoredDatesFromJogSummary(listOfJogSummary = allJogSummaries)
                    )
                    invalidateView()
                },
                { error -> Log.e(VM_TAG, error.localizedMessage, error) }
            ).addTo(compositeDisposable)
    }

    fun getAllJogSummariesAtSpecificDate(date: Date) {
        useCase.getJogSummariesAtDate(date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { listOfModifiedJogSummaries ->
                    viewState = viewState.copy(
                        listOfSpecificDates = convertJogSummaryToRecyclerViewProperties(
                            listOfModifiedJogSummaries
                        )
                    )
                    invalidateView()
                },
                { error -> Log.e(VM_TAG, error.localizedMessage, error) })
            .addTo(compositeDisposable)

    }

    private fun convertJogSummaryToRecyclerViewProperties(listOfModifiedJogSummary: List<ModifiedJogSummary>): List<RecyclerViewProperties> {
        val listOfRecyclerViewProperties = mutableListOf<RecyclerViewProperties>()
        var jogNumber = 1
        listOfModifiedJogSummary.forEach { jogSummary ->
            listOfRecyclerViewProperties.add(
                RecyclerViewProperties(
                    totalDistance = "${jogSummary.totalDistance} Miles",
                    totalTime = getFormattedTime(jogSummary.timeDurationInSeconds),
                    jogEntry = jogNumber.toString(),
                    milesPerHour = getMPH(
                        jogSummary.totalDistance,
                        jogSummary.timeDurationInSeconds
                    )
                ,
                    date = jogSummary.date.print(DateFormat.YYYY_MM_DD.format)
                )
            )
            jogNumber++
        }
        return listOfRecyclerViewProperties
    }

    private fun getColoredDatesFromJogSummary(listOfJogSummary: List<ModifiedJogSummary>): List<ColoredDates> {
        val listOfColoredDates: MutableList<ColoredDates> = mutableListOf()
        if (listOfJogSummary.isEmpty()) {
            viewState.listOfColoredDates.forEach { coloredDates ->
                listOfColoredDates.add(ColoredDates(coloredDates.date, noJogRecorded))
            }
            return listOfColoredDates
        }
        var date = listOfJogSummary[0].date
        var index = 0
        while (date < ZonedDateTime.now()) {
            if (index < listOfJogSummary.size && listOfJogSummary[index].date == date) {
                listOfColoredDates.add(
                    ColoredDates(
                        date = convertZonedDateTimeToDate(date),
                        hasWorkedOutColor
                    )
                )
                index++
            } else {
                listOfColoredDates.add(
                    ColoredDates(
                        date = convertZonedDateTimeToDate(date),
                        hasNotWorkedOutColor
                    )
                )
            }
            date = date.plusDays(1)
        }
        return listOfColoredDates
    }

    private fun convertZonedDateTimeToDate(dateTime: ZonedDateTime): Date =
        Date.from(dateTime.toInstant())

    private fun invalidateView() {
        viewStateObservable.onNext(viewState)
    }
}