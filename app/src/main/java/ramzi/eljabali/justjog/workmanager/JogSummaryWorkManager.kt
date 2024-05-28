package ramzi.eljabali.justjog.workmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.maps.model.LatLng
import javatimefun.zoneddatetime.extensions.toZonedDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ramzi.eljabali.justjog.usecase.JogUseCase
import ramzi.eljabali.justjog.usecase.ModifiedJogEntry
import ramzi.eljabali.justjog.usecase.ModifiedJogSummary
import ramzi.eljabali.justjog.usecase.ModifiedTempJogSummary
import ramzi.eljabali.justjog.util.DateFormat
import ramzi.eljabali.justjog.util.TAG
import ramzi.eljabali.justjog.util.getTotalDistance
import java.time.Duration
import java.time.ZonedDateTime

class JogSummaryWorkManager(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {
    private val jogUseCase by inject<JogUseCase>()
    override suspend fun doWork(): Result =
        withContext(Dispatchers.IO) {
            val currentLatLng = LatLng(
                inputData.getDouble("KEY_LATITUDE", 0.0),
                inputData.getDouble("KEY_LONGITUDE", 0.0)
            )
            val id = inputData.getInt("ID", -1)
            val currentDateTime: ZonedDateTime = inputData.getString("DATE_TIME")
                ?.toZonedDateTime(DateFormat.YYYY_MM_DD_T_TIME.format)!!

            Log.i("JogSummaryWorkManager", "Before GetTempJogSummary")
            try {
                jogUseCase.getJogSummaryTemp(id)
                    .onStart {
                        Log.i("JogSummaryWorkManager", "Start of adding and processing jogs")
                    }
                    .onCompletion {
                        Log.i("JogSummaryWorkManager", "End of adding and processing jogs")
                    }
                    .collect { jogSummaryTemp ->
                        Log.i("JogSummaryWorkManager", "Adding JogEntry")
                        jogUseCase.addJogEntry(
                            ModifiedJogEntry(
                                jogSummaryId = id,
                                dateTime = currentDateTime,
                                latLng = currentLatLng
                            )
                        )
                        Log.i("JogSummaryWorkManager", "Adding JogEntry COMPLETE")
//                        jogSummaryTemp
                        //
                        val modifiedTempJogSummary = if (jogSummaryTemp == null) {
                            Log.i("JogSummaryWorkManager", "Adding First JogSummary Temp")
                            val modifiedTempJogSummary = ModifiedTempJogSummary(
                                jogId = id,
                                date = currentDateTime,
                                location = currentLatLng
                            )
                            jogUseCase.addOrUpdateJogSummaryTemp(
                                modifiedTempJogSummary
                            )
                            Log.i("JogSummaryWorkManager", "Adding First JogSummary Temp COMPLETE")
                            modifiedTempJogSummary
                        } else {
                            Log.i("JogSummaryWorkManager", "Updating JogSummary Temp")
                            val updatedDistance = jogSummaryTemp.totalDistance +
                                    getTotalDistance(listOf(jogSummaryTemp.location, currentLatLng))
                            val updatedDuration =
                                Duration.between(jogSummaryTemp.date, currentDateTime)
                            val modifiedTempJogSummary = ModifiedTempJogSummary(
                                jogId = id,
                                date = currentDateTime,
                                totalDistance = updatedDistance,
                                duration = updatedDuration,
                            )
                            jogUseCase.addOrUpdateJogSummaryTemp(modifiedTempJogSummary)
                            Log.i("JogSummaryWorkManager", "Updating JogSummary Temp COMPLETE")
                            modifiedTempJogSummary
                        }

                        Log.i("JogSummaryWorkManager", "Adding/Updating JogSummary")
                        val modifiedJogSummary = ModifiedJogSummary(
                            jogId = modifiedTempJogSummary.jogId,
                            startDate = modifiedTempJogSummary.date,
                            duration = modifiedTempJogSummary.duration,
                            totalDistance = modifiedTempJogSummary.totalDistance
                        )
                        jogUseCase.addJogSummary(modifiedJogSummary)
                        Log.i("JogSummaryWorkManager", "Adding/Updating JogSummary COMPLETED")
                    }
                Result.success()
            } catch (e: Exception) {
                Log.e(this.javaClass.simpleName, "Exception: ${e.message}")
                Result.failure()
            }
        }
}