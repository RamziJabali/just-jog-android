package com.eljabali.joggingapplicationandroid.data.usecase

import android.util.Log
import com.eljabali.joggingapplicationandroid.data.repo.jogentries.JogEntries
import com.eljabali.joggingapplicationandroid.data.repo.jogentries.JogEntriesRepository
import com.eljabali.joggingapplicationandroid.data.repo.jogsummary.JogSummary
import com.eljabali.joggingapplicationandroid.data.repo.jogsummary.JogSummaryRepository
import com.eljabali.joggingapplicationandroid.util.DateFormat
import com.eljabali.joggingapplicationandroid.util.TAG
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
import java.time.Duration
import java.time.LocalDate
import java.time.ZonedDateTime

class UseCase(
    private val jogEntriesRepository: JogEntriesRepository,
    private val jogSummaryRepository: JogSummaryRepository
) {

    private val compositeDisposable = CompositeDisposable()

    fun addJog(modifiedJogDateInformation: ModifiedJogDateInformation): Completable =
        jogEntriesRepository.add(
            convertModifiedJogDateInformationToWorkOutDate(modifiedJogDateInformation)
        )

    fun getAllJogs(): Observable<List<ModifiedJogDateInformation>> =
        jogEntriesRepository.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { listOfAllJogDates ->
                return@map listOfAllJogDates.map { workoutDate ->
                    convertWorkOutDateToModifiedJogDate(workoutDate)
                }
            }

    fun getNewRunID(): Maybe<Int> =
        jogSummaryRepository.getLast().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { workoutDate ->
                return@map workoutDate.id + 1
            }


    fun getRangeOfJogsBetweenStartAndEndDate(
        startDate: LocalDate,
        endDate: LocalDate
    ): Observable<List<ModifiedJogDateInformation>> =
        jogEntriesRepository.getByRangeOfDates(
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

    fun getAllJogsAtSpecificDate(localDate: LocalDate): Maybe<List<ModifiedJogDateInformation>> =
        jogEntriesRepository.getByDate(date = "%${localDate.print(DateFormat.YYYY_MM_DD.format)}%")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { listOfSpecificJogDates ->
                return@map listOfSpecificJogDates.map { workoutDate ->
                    convertWorkOutDateToModifiedJogDate(workoutDate)
                }
            }

    fun getJogEntriesById(jogID: Int): Maybe<List<ModifiedJogDateInformation>> =
        jogEntriesRepository.getByJogID(jogID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { listOfSpecificJogDates ->
                return@map listOfSpecificJogDates.map { workoutDate ->
                    convertWorkOutDateToModifiedJogDate(workoutDate)
                }
            }

    fun getAllJogSummaries(): Observable<List<ModifiedJogSummary>> =
        jogSummaryRepository.getAll().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { listOfJogSummaries ->
                return@map listOfJogSummaries.map { jogSummary ->
                    ModifiedJogSummary(
                        jogId = jogSummary.id,
                        date = jogSummary.startDate.parseZonedDateTime()!!,
                        timeDurationInSeconds = jogSummary.totalJogDuration,
                        totalDistance = jogSummary.totalJogDistance
                    )
                }
            }

    fun addJogSummary(
        startDate: ZonedDateTime,
        jogId: Int,
        endDate: ZonedDateTime,
        totalJogDistance: Double
    ): Completable =
        jogSummaryRepository.add(
            JogSummary(
                jogId,
                startDate.print(DateFormat.YYYY_MM_DD_T_TIME.format),
                Duration.between(startDate, endDate).seconds,
                totalJogDistance
            )
        )

    fun getJogSummariesAtDate(localDate: LocalDate): Maybe<List<ModifiedJogSummary>> =
        jogSummaryRepository.getByDate(
            date = "%${localDate.print(DateFormat.YYYY_MM_DD.format)}%"
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { listOfJogSummaries ->
                return@map listOfJogSummaries.map { jogSummary ->
                    ModifiedJogSummary(
                        jogId = jogSummary.id,
                        date = jogSummary.startDate.parseZonedDateTime()!!,
                        timeDurationInSeconds = jogSummary.totalJogDuration,
                        totalDistance = jogSummary.totalJogDistance
                    )
                }
            }

    fun getGetJogSummariesBetweenDates(startDate: LocalDate, endDate: LocalDate): Observable<List<ModifiedJogSummary>> =
        jogSummaryRepository.getByRangeOfDates(
            startDate = startDate.print(DateFormat.YYYY_MM_DD.format),
            endDate = endDate.print(DateFormat.YYYY_MM_DD.format)
        )  .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { listOfJogSummaries ->
                return@map listOfJogSummaries.map { jogSummary ->
                    ModifiedJogSummary(
                        jogId = jogSummary.id,
                        date = jogSummary.startDate.parseZonedDateTime()!!,
                        timeDurationInSeconds = jogSummary.totalJogDuration,
                        totalDistance = jogSummary.totalJogDistance
                    )
                }
    }

    fun deleteAllEntries() {
        jogEntriesRepository.deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Log.i(TAG, "Success") },
                { error -> Log.e(TAG, error.localizedMessage, error) }
            )
            .addTo(compositeDisposable)

        jogSummaryRepository.deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Log.i(TAG, "Success") },
                { error -> Log.e(TAG, error.localizedMessage, error) })
            .addTo(compositeDisposable)
    }

    private fun convertModifiedJogDateInformationToWorkOutDate(modifiedJogDateInformation: ModifiedJogDateInformation): JogEntries =
        JogEntries(
            dateTime = modifiedJogDateInformation.dateTime.print(DateFormat.YYYY_MM_DD_T_TIME.format),
            jogSummaryID = modifiedJogDateInformation.runNumber,
            latitude = modifiedJogDateInformation.latitudeLongitude.latitude,
            longitude = modifiedJogDateInformation.latitudeLongitude.longitude
        )

    private fun convertWorkOutDateToModifiedJogDate(jogEntries: JogEntries): ModifiedJogDateInformation =
        ModifiedJogDateInformation(
            dateTime = jogEntries.dateTime.parseZonedDateTime()!!,
            runNumber = jogEntries.jogSummaryID,
            latitudeLongitude = LatLng(jogEntries.latitude, jogEntries.longitude),
        )
}