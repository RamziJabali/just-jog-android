package com.eljabali.joggingapplicationandroid.usecase

import android.util.Log
import com.eljabali.joggingapplicationandroid.libraries.DateFormat
import com.eljabali.joggingapplicationandroid.repo.WorkoutDate
import com.eljabali.joggingapplicationandroid.repo.WorkoutRepository
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import localdate.extensions.print
import zoneddatetime.extensions.parseZonedDateTime
import zoneddatetime.extensions.print
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class UseCase(private val repository: WorkoutRepository) {

    companion object {
        const val UC_TAG = "USECASE"
    }

    private val compositeDisposable = CompositeDisposable()

    fun addJog(modifiedJogDateInformation: ModifiedJogDateInformation): Completable =
        repository.addWorkoutDate(
            convertModifiedJogDateInformationToWorkOutDate(modifiedJogDateInformation)
        )

    fun getAllJogs(): Observable<List<ModifiedJogDateInformation>> =
        repository.getAllWorkoutDates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { listOfAllJogDates ->
                return@map listOfAllJogDates.map { workoutDate ->
                    convertWorkOutDateToModifiedJogDate(workoutDate)
                }
            }

    fun getRangeOfJogsBetweenStartAndEndDate(
        startDate: LocalDate,
        endDate: LocalDate
    ): Observable<List<ModifiedJogDateInformation>> =
        repository.getRangeOfDates(
            startDate =startDate.print(DateFormat.YYYY_MM_DD.format),
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
        repository.getWorkoutDate(date = "%${convertDateToZonedDateTime(date).print(DateFormat.YYYY_MM_DD.format)}%")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { listOfSpecificJogDates ->
                return@map listOfSpecificJogDates.map { workoutDate ->
                    convertWorkOutDateToModifiedJogDate(workoutDate)
                }
            }

    fun getAllJogsAtSpecificDate(localDate: LocalDate): Maybe<List<ModifiedJogDateInformation>> =
        repository.getWorkoutDate(date = "%${localDate.print(DateFormat.YYYY_MM_DD.format)}%")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { listOfSpecificJogDates ->
                return@map listOfSpecificJogDates.map { workoutDate ->
                    convertWorkOutDateToModifiedJogDate(workoutDate)
                }
            }


    private fun convertDateToZonedDateTime(date: Date): ZonedDateTime =
        ZonedDateTime.ofInstant(
            date.toInstant(),
            ZoneId.systemDefault()
        )


    fun deleteAllEntries() {
        compositeDisposable.add(
            repository.deleteAllWorkoutDates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { Log.i(UC_TAG, "Success") },
                    { error -> Log.e(UC_TAG, error.localizedMessage, error) }
                )
        )
    }


    private fun convertModifiedJogDateInformationToWorkOutDate(modifiedJogDateInformation: ModifiedJogDateInformation): WorkoutDate =
        WorkoutDate(
            totalRuns = 0,
            dateTime = modifiedJogDateInformation.dateTime.print(DateFormat.YYYY_MM_DD_T_TIME.format),
            runNumber = modifiedJogDateInformation.runNumber,
            latitude = modifiedJogDateInformation.latitudeLongitude.latitude,
            longitude = modifiedJogDateInformation.latitudeLongitude.longitude
        )

    private fun convertWorkOutDateToModifiedJogDate(workoutDate: WorkoutDate): ModifiedJogDateInformation =
        ModifiedJogDateInformation(
            dateTime = workoutDate.dateTime.parseZonedDateTime()!!,
            runNumber = workoutDate.runNumber,
            latitudeLongitude = LatLng(workoutDate.latitude, workoutDate.longitude),
        )
}