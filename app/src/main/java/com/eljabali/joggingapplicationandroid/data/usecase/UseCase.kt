package com.eljabali.joggingapplicationandroid.data.usecase

import android.util.Log
import com.eljabali.joggingapplicationandroid.data.repo.calendar.JogSummary
import com.eljabali.joggingapplicationandroid.data.repo.calendar.JogSummaryRepository
import com.eljabali.joggingapplicationandroid.util.DateFormat
import com.eljabali.joggingapplicationandroid.data.repo.jog.JogDate
import com.eljabali.joggingapplicationandroid.data.repo.jog.JogRepository
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import localdate.extensions.print
import zoneddatetime.extensions.parseZonedDateTime
import zoneddatetime.extensions.print
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class UseCase(
    private val jogRepository: JogRepository,
    private val jogSummaryRepository: JogSummaryRepository
) {

    companion object {
        const val UC_TAG = "USECASE"
    }

    private val compositeDisposable = CompositeDisposable()

    fun addJog(modifiedJogDateInformation: ModifiedJogDateInformation): Completable =
        jogRepository.addWorkoutDate(
            convertModifiedJogDateInformationToWorkOutDate(modifiedJogDateInformation)
        )

    fun getAllJogs(): Observable<List<ModifiedJogDateInformation>> =
        jogRepository.getAllWorkoutDates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { listOfAllJogDates ->
                return@map listOfAllJogDates.map { workoutDate ->
                    convertWorkOutDateToModifiedJogDate(workoutDate)
                }
            }

    fun getNewRunID(): Maybe<Int> =
        jogRepository.getLastRunID().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { workoutDate ->
                return@map workoutDate.runNumber + 1
            }


    fun getRangeOfJogsBetweenStartAndEndDate(
        startDate: LocalDate,
        endDate: LocalDate
    ): Observable<List<ModifiedJogDateInformation>> =
        jogRepository.getRangeOfDates(
            startDate = startDate.print(DateFormat.YYYY_MM_DD.format),
            endDate = endDate.print(DateFormat.YYYY_MM_DD.format)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { listOfSpecificJogDates ->
                return@map listOfSpecificJogDates.map { workoutDate ->
                    convertWorkOutDateToModifiedJogDate(workoutDate)
                }
            }

    fun getAllJogsAtSpecificDate(date: Date): Maybe<List<ModifiedJogDateInformation>> =
        jogRepository.getWorkoutDate(date = "%${convertDateToZonedDateTime(date).print(DateFormat.YYYY_MM_DD.format)}%")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { listOfSpecificJogDates ->
                return@map listOfSpecificJogDates.map { workoutDate ->
                    convertWorkOutDateToModifiedJogDate(workoutDate)
                }
            }

    fun getAllJogsAtSpecificDate(localDate: LocalDate): Maybe<List<ModifiedJogDateInformation>> =
        jogRepository.getWorkoutDate(date = "%${localDate.print(DateFormat.YYYY_MM_DD.format)}%")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { listOfSpecificJogDates ->
                return@map listOfSpecificJogDates.map { workoutDate ->
                    convertWorkOutDateToModifiedJogDate(workoutDate)
                }
            }

    fun getAllJogSummaries(): Observable<List<ModifiedJogSummary>> =
        jogSummaryRepository.getAllJogSummaries().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { listOfJogSummaries ->
                return@map listOfJogSummaries.map { jogSummary ->
                    ModifiedJogSummary(
                        jogId = jogSummary.jogId,
                        date = jogSummary.jogStartDate.parseZonedDateTime()!!
                    )
                }
            }

    fun addJogSummary(startDate: ZonedDateTime, jogNumber: Int): Completable =
        jogSummaryRepository.addJogDate(
            JogSummary(
                jogNumber,
                startDate.print(DateFormat.YYYY_MM_DD.format),
            )
        )

    fun getJogSummariesAtDate(localDate: LocalDate): Maybe<List<ModifiedJogSummary>> =
        jogSummaryRepository.getJogDate(date = "%${localDate.print(DateFormat.YYYY_MM_DD.format)}%")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { listOfJogSummaries ->
                return@map listOfJogSummaries.map { jogSummary ->
                    ModifiedJogSummary(
                        jogId = jogSummary.jogId,
                        date = jogSummary.jogStartDate.parseZonedDateTime()!!
                    )
                }
            }


    private fun convertDateToZonedDateTime(date: Date): ZonedDateTime =
        ZonedDateTime.ofInstant(
            date.toInstant(),
            ZoneId.systemDefault()
        )

    fun deleteAllEntries() {
        jogRepository.deleteAllWorkoutDates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Log.i(UC_TAG, "Success") },
                { error -> Log.e(UC_TAG, error.localizedMessage, error) }
            )
            .addTo(compositeDisposable)

        jogSummaryRepository.deleteAllJogDates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Log.i(UC_TAG, "Success") },
                { error -> Log.e(UC_TAG, error.localizedMessage, error) })
            .addTo(compositeDisposable)
    }

    private fun convertModifiedJogDateInformationToWorkOutDate(modifiedJogDateInformation: ModifiedJogDateInformation): JogDate =
        JogDate(
            totalRuns = 0,
            dateTime = modifiedJogDateInformation.dateTime.print(DateFormat.YYYY_MM_DD_T_TIME.format),
            runNumber = modifiedJogDateInformation.runNumber,
            latitude = modifiedJogDateInformation.latitudeLongitude.latitude,
            longitude = modifiedJogDateInformation.latitudeLongitude.longitude
        )

    private fun convertWorkOutDateToModifiedJogDate(jogDate: JogDate): ModifiedJogDateInformation =
        ModifiedJogDateInformation(
            dateTime = jogDate.dateTime.parseZonedDateTime()!!,
            runNumber = jogDate.runNumber,
            latitudeLongitude = LatLng(jogDate.latitude, jogDate.longitude),
        )
}