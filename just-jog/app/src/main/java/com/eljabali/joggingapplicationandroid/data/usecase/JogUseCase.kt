package com.eljabali.joggingapplicationandroid.data.usecase

import com.eljabali.joggingapplicationandroid.data.repo.jogentries.JogEntries
import com.eljabali.joggingapplicationandroid.data.repo.jogentries.JogEntriesRepository
import com.eljabali.joggingapplicationandroid.data.repo.jogsummary.JogSummary
import com.eljabali.joggingapplicationandroid.data.repo.jogsummary.JogSummaryRepository
import com.eljabali.joggingapplicationandroid.data.repo.jogsummarytemp.JogSummaryTemp
import com.eljabali.joggingapplicationandroid.data.repo.jogsummarytemp.JogSummaryTempRepository
import com.eljabali.joggingapplicationandroid.motivationalquotes.MotivationalQuotes
import com.eljabali.joggingapplicationandroid.motivationalquotes.MotivationalQuotesAPIRepository
import com.eljabali.joggingapplicationandroid.util.DateFormat
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import localdate.extensions.print
import zoneddatetime.extensions.parseZonedDateTime
import zoneddatetime.extensions.print
import java.time.Duration
import java.time.LocalDate
import java.time.ZonedDateTime

class JogUseCase(
    private val jogEntriesRepository: JogEntriesRepository,
    private val jogSummaryRepository: JogSummaryRepository,
    private val tempJogSummaryRepository: JogSummaryTempRepository,
    private val motivationalQuotesAPIRepository: MotivationalQuotesAPIRepository
) {

    private val compositeDisposable = CompositeDisposable()

    fun addJogEntry(modifiedJogDateInformation: ModifiedJogDateInformation): Completable =
        jogEntriesRepository.add(
            convertModifiedJogDateInformationToWorkOutDate(modifiedJogDateInformation)
        )

    fun getAllJogs(): Observable<List<ModifiedJogDateInformation>> =
        jogEntriesRepository.getAll()
            .map { listOfAllJogDates ->
                return@map listOfAllJogDates.map { workoutDate ->
                    convertWorkOutDateToModifiedJogDate(workoutDate)
                }
            }

    fun getNewRunID(): Maybe<Int> =
        jogSummaryRepository.getLast()
            .map { workoutDate ->
                return@map workoutDate.id + 1
            }

    fun getLastJogSummary(): Maybe<JogSummary> =
        jogSummaryRepository.getLast()


    fun getRangeOfJogsBetweenStartAndEndDate(
        startDate: LocalDate,
        endDate: LocalDate
    ): Observable<List<ModifiedJogDateInformation>> =
        jogEntriesRepository.getByRangeOfDates(
            startDate = startDate.print(DateFormat.YYYY_MM_DD.format),
            endDate = endDate.print(DateFormat.YYYY_MM_DD.format)
        )
            .map { listOfSpecificJogDates ->
                return@map listOfSpecificJogDates.map { workoutDate ->
                    convertWorkOutDateToModifiedJogDate(workoutDate)
                }
            }

    fun getAllJogsAtSpecificDate(localDate: LocalDate): Maybe<List<ModifiedJogDateInformation>> =
        jogEntriesRepository.getByDate(date = "%${localDate.print(DateFormat.YYYY_MM_DD.format)}%")
            .map { listOfSpecificJogDates ->
                return@map listOfSpecificJogDates.map { workoutDate ->
                    convertWorkOutDateToModifiedJogDate(workoutDate)
                }
            }

    fun getJogEntriesById(jogID: Int): Maybe<List<ModifiedJogDateInformation>> =
        jogEntriesRepository.getByJogID(jogID)
            .map { listOfSpecificJogDates ->
                return@map listOfSpecificJogDates.map { workoutDate ->
                    convertWorkOutDateToModifiedJogDate(workoutDate)
                }
            }

    fun getAllJogSummaries(): Observable<List<ModifiedJogSummary>> =
        jogSummaryRepository.getAll()
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
        endDate: ZonedDateTime,
        jogId: Int,
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

    fun addJogSummary(
        startDate: ZonedDateTime,
        duration: Duration,
        jogId: Int,
        totalJogDistance: Double
    ): Completable =
        jogSummaryRepository.add(
            JogSummary(
                jogId,
                startDate.print(DateFormat.YYYY_MM_DD_T_TIME.format),
                duration.seconds,
                totalJogDistance
            )
        )

    fun addTempJogSummary(
        currentDateTime: ZonedDateTime,
        jogId: Int,
        totalJogDistance: Double,
        duration: Duration,
        latLng: LatLng
    ): Completable =
        tempJogSummaryRepository.add(
            JogSummaryTemp(
                jogId,
                currentDateTime.print(DateFormat.YYYY_MM_DD_T_TIME.format),
                duration.seconds,
                totalJogDistance,
                latitude = latLng.latitude,
                longitude = latLng.longitude
            )
        )

    fun getTempJogSummary(id:Int): Maybe<ModifiedTempJogSummary> =
        tempJogSummaryRepository.getByID(id).map { tempJogSummary ->
            ModifiedTempJogSummary(
                tempJogSummary.id,
                tempJogSummary.currentDateTime.parseZonedDateTime()!!,
                tempJogSummary.totalJogDistance,
                tempJogSummary.totalJogDuration,
                LatLng(tempJogSummary.latitude, tempJogSummary.longitude)
            )
        }


    fun getJogSummariesAtDate(localDate: LocalDate): Maybe<List<ModifiedJogSummary>> =
        jogSummaryRepository.getByDate(
            date = "%${localDate.print(DateFormat.YYYY_MM_DD.format)}%"
        )
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

    fun getJogSummariesBetweenDates(
        startDate: ZonedDateTime,
        endDate: ZonedDateTime
    ): Observable<List<ModifiedJogSummary>> =
        jogSummaryRepository.getByRangeOfDates(
            startDate = startDate,
            endDate = endDate
        )
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

    fun getRandomMotivationalQuote(): Observable<MotivationalQuotes> =
        motivationalQuotesAPIRepository
            .getMotivationalQuote()

    fun deleteAllJogEntries() = jogEntriesRepository.deleteAll()

    fun deleteAllJogSummaries() = jogSummaryRepository.deleteAll()

    fun deleteAllJogSummariesTemp() = tempJogSummaryRepository.deleteAll()

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

