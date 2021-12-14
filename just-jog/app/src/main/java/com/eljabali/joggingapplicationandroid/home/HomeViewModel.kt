package com.eljabali.joggingapplicationandroid.home

import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.eljabali.joggingapplicationandroid.calendar.jogsummaries.JogSummaryProperties
import com.eljabali.joggingapplicationandroid.data.usecase.JogUseCase
import com.eljabali.joggingapplicationandroid.data.usecase.ModifiedJogSummary
import com.eljabali.joggingapplicationandroid.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import localdate.LocalDateUtil
import zoneddatetime.extensions.getDaysInMonth
import zoneddatetime.extensions.isBeforeEqualDay
import zoneddatetime.extensions.print
import java.lang.StringBuilder
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class HomeViewModel(
    application: Application,
    private val jogUseCase: JogUseCase
) : AndroidViewModel(application) {

    companion object {
        val hasWorkedOutColor: ColorDrawable = ColorDrawable(Color.GREEN)
        val hasNotWorkedOutColor: ColorDrawable = ColorDrawable(Color.RED)
        val noJogRecorded: ColorDrawable = ColorDrawable(Color.WHITE)
    }

    val viewStateLiveData: MutableLiveData<HomeViewState> by lazy {
        MutableLiveData<HomeViewState>()
    }
    private val compositeDisposable = CompositeDisposable()
    private var viewState = HomeViewState()

    fun getAllDates(startDate: ZonedDateTime) {
        val officialStartDate =
            ZonedDateTime.of(startDate.year, startDate.monthValue, 1, 0, 0, 0, 0, startDate.zone)
        val officialEndDate = ZonedDateTime.of(
            startDate.year,
            startDate.monthValue,
            startDate.getDaysInMonth(),
            0,
            0,
            0,
            0,
            startDate.zone
        )
        jogUseCase.getJogSummariesBetweenDates(officialStartDate, officialEndDate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { allJogSummaries ->
                    viewState = viewState.copy(
                        listOfColoredDates = getColoredDatesFromJogSummary(listOfJogSummary = allJogSummaries)
                    )
                    invalidateView()
                },
                { error -> Log.e(TAG, error.localizedMessage, error) }
            ).addTo(compositeDisposable)
    }

    fun getAllDates(year: Int, month: Int) {
        val officialStartDate = ZonedDateTime.of(year, month, 1, 0, 0, 0, 0, ZoneId.systemDefault())
        val officialEndDate = ZonedDateTime.of(
            year,
            month,
            officialStartDate.getDaysInMonth(),
            0,
            0,
            0,
            0,
            ZoneId.systemDefault()
        )
        jogUseCase.getJogSummariesBetweenDates(officialStartDate, officialEndDate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { allJogSummaries ->
                    viewState = viewState.copy(
                        listOfColoredDates = getColoredDatesFromJogSummary(listOfJogSummary = allJogSummaries)
                    )
                    invalidateView()
                },
                { error -> Log.e(TAG, error.localizedMessage, error) }
            ).addTo(compositeDisposable)
    }

    fun getAllJogSummariesAtSpecificDate(date: Date) {
        val localDateTimeSelected = LocalDateUtil.new(date)
        jogUseCase.getJogSummariesAtDate(localDateTimeSelected)
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
                { error -> Log.e(TAG, error.localizedMessage, error) })
            .addTo(compositeDisposable)

    }

    private fun convertJogSummaryToRecyclerViewProperties(listOfModifiedJogSummary: List<ModifiedJogSummary>): List<JogSummaryProperties> {
        val listOfRecyclerViewProperties = mutableListOf<JogSummaryProperties>()
        var jogNumber = 1
        listOfModifiedJogSummary.forEach { jogSummary ->
            val totalDistance = String.format("%.2f",jogSummary.totalDistance)
            listOfRecyclerViewProperties.add(
                JogSummaryProperties(
                    jogSummaryId = jogSummary.jogId.toString(),
                    totalDistance = "$totalDistance Miles",
                    totalTime = getFormattedTime(
                        jogSummary.timeDurationInSeconds,
                        DurationFormat.H_M_S
                    ),
                    jogEntryCountOfDay = jogNumber.toString(),
                    milesPerHour = getMPH(
                        jogSummary.totalDistance,
                        jogSummary.timeDurationInSeconds
                    ),
                    date = jogSummary.date.print(DateFormat.YYYY_MM_DD.format),
                    startTime = jogSummary.date.print(DateFormat.HH_MM_SS.format)
                )
            )
            jogNumber++
        }
        return listOfRecyclerViewProperties
    }

    private fun getColoredDatesFromJogSummary(listOfJogSummary: List<ModifiedJogSummary>): List<ColoredDates> {
        val listOfColoredDates: MutableList<ColoredDates> = mutableListOf()
        if (listOfJogSummary.isEmpty()) {
            var officialStartDate =
                ZonedDateTime.of(
                    ZonedDateTime.now().year,
                    ZonedDateTime.now().monthValue,
                    1,
                    0,
                    0,
                    0,
                    0,
                    ZonedDateTime.now().zone
                )
            while (officialStartDate.isBeforeEqualDay(ZonedDateTime.now())) {
                listOfColoredDates.add(
                    ColoredDates(
                        date = convertZonedDateTimeToDate(officialStartDate),
                        hasNotWorkedOutColor
                    )
                )
                officialStartDate = officialStartDate.plusDays(1)
            }
            return listOfColoredDates
        }

        var date = listOfJogSummary[0].date
        var index = 0
        val timeNow = LocalDate.now()
        while (date.toLocalDate() <= timeNow) {
            if (index < listOfJogSummary.size && listOfJogSummary[index].date.toLocalDate() == date.toLocalDate()) {
                listOfColoredDates.add(
                    ColoredDates(
                        date = convertZonedDateTimeToDate(date),
                        hasWorkedOutColor
                    )
                )
                do {
                    index++
                } while (index < listOfJogSummary.size && date.toLocalDate() == listOfJogSummary[index].date.toLocalDate())
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
        viewStateLiveData.value = viewState
    }
}