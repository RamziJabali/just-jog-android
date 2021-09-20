package com.eljabali.joggingapplicationandroid.usecase

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.annotation.ColorRes
import com.eljabali.joggingapplicationandroid.repo.WorkoutDate
import com.eljabali.joggingapplicationandroid.repo.WorkoutRepository
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class UseCase(private val workoutRepository: WorkoutRepository) {

    companion object {
        private val hasWorkedOutColor: ColorDrawable =
            ColorDrawable(Color.GREEN)
    }

    fun addJog(modifiedJogDateInformation: ModifiedJogDateInformation) {
        workoutRepository.addWorkoutDate(
            convertModifiedJogDateInformationToWorkOutDate(modifiedJogDateInformation)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    @SuppressLint("CheckResult")
    fun getAllJogs(): Observable<List<ModifiedJogDateInformation>> =
        workoutRepository.getAllWorkoutDates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { listOfAllJogDates ->
                return@map listOfAllJogDates.map { workoutDate ->
                    convertWorkOutDateToModifiedJogDate(workoutDate)
                }
            }

    @SuppressLint("CheckResult")
    fun getAllJogsAtSpecificDate(date: Date): Maybe<List<ModifiedJogDateInformation>> =
        workoutRepository.getWorkoutDate(date = date.time)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { listOfSpecificJogDates ->
                return@map listOfSpecificJogDates.map { workoutDate ->
                    convertWorkOutDateToModifiedJogDate(workoutDate)
                }
            }

    private fun convertModifiedJogDateInformationToWorkOutDate(modifiedJogDateInformation: ModifiedJogDateInformation): WorkoutDate =
        WorkoutDate(
            date = modifiedJogDateInformation.date.time,
            runNumber = modifiedJogDateInformation.runNumber,
            didUserAttend = true,
            time = modifiedJogDateInformation.time,
            latitude = modifiedJogDateInformation.latitudeLongitude.latitude,
            longitude = modifiedJogDateInformation.latitudeLongitude.longitude
        )

    private fun convertWorkOutDateToModifiedJogDate(workoutDate: WorkoutDate): ModifiedJogDateInformation =
        ModifiedJogDateInformation(
            date = Date(workoutDate.date),
            runNumber = workoutDate.runNumber,
            backgroundColor = hasWorkedOutColor,
            time = workoutDate.time,
            latitudeLongitude = LatLng(workoutDate.latitude, workoutDate.longitude)
        )


}