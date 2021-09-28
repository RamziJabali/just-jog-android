package com.eljabali.joggingapplicationandroid.usecase

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import com.eljabali.joggingapplicationandroid.repo.WorkoutDate
import com.eljabali.joggingapplicationandroid.repo.WorkoutRepository
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import zoneddatetime.extensions.parseZonedDateTime
import zoneddatetime.extensions.print
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class UseCase(private val repository: WorkoutRepository) {

    companion object {
        const val UC_TAG = "USECASE"
    }

    fun addJog(modifiedJogDateInformation: ModifiedJogDateInformation): Completable =
        repository.addWorkoutDate(
            convertModifiedJogDateInformationToWorkOutDate(modifiedJogDateInformation)
        )

    @SuppressLint("CheckResult")
    fun getAllJogs(): Observable<List<ModifiedJogDateInformation>> =
        repository.getAllWorkoutDates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { listOfAllJogDates ->
                return@map listOfAllJogDates.map { workoutDate ->
                    convertWorkOutDateToModifiedJogDate(workoutDate)
                }
            }

    @SuppressLint("CheckResult")
    fun getAllJogsAtSpecificDate(date: Date): Maybe<List<ModifiedJogDateInformation>> =
        repository.getWorkoutDate(date = convertDateToZonedDateTime(date).print("yyyy-MM-dd"))
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


    @SuppressLint("CheckResult")
    fun deleteAllEntries() {
        repository.deleteAllWorkoutDates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Log.i(UC_TAG, "Success") },
                { error -> Log.e(UC_TAG, error.localizedMessage, error) }
            )

    }


    private fun convertModifiedJogDateInformationToWorkOutDate(modifiedJogDateInformation: ModifiedJogDateInformation): WorkoutDate =
        WorkoutDate(
            totalRuns = 0,
            date = modifiedJogDateInformation.date.print("yyyy-MM-dd"),
            hours = modifiedJogDateInformation.date.hour,
            minutes = modifiedJogDateInformation.date.minute,
            seconds = modifiedJogDateInformation.date.second,
            runNumber = modifiedJogDateInformation.runNumber,
            latitude = modifiedJogDateInformation.latitudeLongitude.latitude,
            longitude = modifiedJogDateInformation.latitudeLongitude.longitude
        )

    private fun convertWorkOutDateToModifiedJogDate(workoutDate: WorkoutDate): ModifiedJogDateInformation =
        ModifiedJogDateInformation(
            date = workoutDate.date.parseZonedDateTime()!!,
            runNumber = workoutDate.runNumber,
            latitudeLongitude = LatLng(workoutDate.latitude, workoutDate.longitude),
            hours = workoutDate.hours,
            minutes = workoutDate.minutes,
            seconds = workoutDate.seconds,
        )


}