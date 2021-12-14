package com.eljabali.joggingapplicationandroid

import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.RxWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.eljabali.joggingapplicationandroid.data.usecase.JogUseCase
import com.eljabali.joggingapplicationandroid.data.usecase.ModifiedJogDateInformation
import com.eljabali.joggingapplicationandroid.util.DateFormat
import com.eljabali.joggingapplicationandroid.util.TAG
import com.eljabali.joggingapplicationandroid.util.getTotalDistance
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject
import zoneddatetime.extensions.parseZonedDateTime
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class RecordSummaryWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters), KoinComponent {
    private val jogUseCase: JogUseCase by inject()
    private val compositeDisposable = CompositeDisposable()

    override fun doWork(): Result {
        val currentLatLng = LatLng(
            inputData.getDouble("KEY_LATITUDE", 0.0),
            inputData.getDouble("KEY_LONGITUDE", 0.0)
        )
        val id = inputData.getInt("ID", -1)
        val currentDateTime: ZonedDateTime = inputData.getString("DATE_TIME")
            ?.parseZonedDateTime(DateFormat.YYYY_MM_DD_T_TIME.format)!!

        Log.i(TAG, "Before GetTempJogSummary")
        jogUseCase.getTempJogSummary(id)
            .map { tempJogSummary ->
                Log.i(TAG, "Adding Jog Entry")
                jogUseCase.addJogEntry(
                    ModifiedJogDateInformation(
                        currentDateTime,
                        id,
                        currentLatLng
                    )
                ).subscribe()
                tempJogSummary
            }.map { tempJogSummary ->
                Log.i(TAG, "Adding Temp Jog")
                val updatedDistance = tempJogSummary.totalDistance +
                        getTotalDistance(listOf(tempJogSummary.location, currentLatLng))
                val updatedDuration =
                    Duration.between(tempJogSummary.date, currentDateTime)
                jogUseCase.addTempJogSummary(
                    tempJogSummary.date,
                    id,
                    updatedDistance,
                    updatedDuration,
                    currentLatLng
                ).subscribe()
                Triple(tempJogSummary, updatedDistance, updatedDuration)
            }.map { triple ->
                Log.i(TAG, "Adding Jog Summary")
                jogUseCase.addJogSummary(
                    triple.first.date,
                    triple.third,
                    id,
                    triple.second
                ).subscribe()
                triple.first
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    Log.i(TAG, "Successfully Ran")
                },
                { error -> Log.e(TAG, error.localizedMessage, error) },
                {
                    Log.i(TAG, "Temp Entry Does Not Exist")
                    jogUseCase.addTempJogSummary(
                        currentDateTime,
                        id,
                        0.0,
                        Duration.ZERO,
                        currentLatLng
                    ).subscribe()
                    jogUseCase.addJogSummary(
                        currentDateTime,
                        Duration.ZERO,
                        id,
                        0.0
                    ).subscribe()
                    jogUseCase.addJogEntry(
                        ModifiedJogDateInformation(
                            currentDateTime,
                            id,
                            currentLatLng
                        )
                    )
                        .subscribe()
                }
            ).addTo(compositeDisposable)
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        compositeDisposable.clear()
    }
}
