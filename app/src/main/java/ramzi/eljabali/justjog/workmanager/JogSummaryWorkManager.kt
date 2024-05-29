package ramzi.eljabali.justjog.workmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.maps.model.LatLng
import javatimefun.zoneddatetime.extensions.toZonedDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ramzi.eljabali.justjog.repository.room.jogsummarytemp.JogSummaryTemp
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
    override suspend fun doWork(): Result {
        val currentLatLng = LatLng(
            inputData.getDouble("KEY_LATITUDE", 0.0),
            inputData.getDouble("KEY_LONGITUDE", 0.0)
        )
        val id = inputData.getInt("ID", -1)
        val currentDateTime: ZonedDateTime = inputData.getString("DATE_TIME")
            ?.toZonedDateTime(DateFormat.YYYY_MM_DD_T_TIME.format)!!

        var jogSummaryTemp: ModifiedTempJogSummary? = null
            withContext(Dispatchers.IO) {
                try {
                    Log.i("JogSummaryWorkManager", "Before GetTempJogSummary")
                    jogSummaryTemp = jogUseCase.getJogSummaryTemp(id)
                        .onStart {
                            Log.i("JogSummaryWorkManager", "Start of adding and processing jogs")
                        }
                        .onCompletion {
                            Log.i("JogSummaryWorkManager", "End of adding and processing jogs")
                        }
                        .firstOrNull()
                } catch (e: Exception) {
                    Log.e(TAG, "Exception: ${e.message}")
                    return@withContext Result.failure()
                }
            }

        withContext(Dispatchers.IO) {
            try {
                Log.i("JogSummaryWorkManager", "Adding JogEntry")
                jogUseCase.addJogEntry(
                    ModifiedJogEntry(
                        jogSummaryId = id,
                        dateTime = currentDateTime,
                        latLng = currentLatLng
                    )
                )
                Log.i("JogSummaryWorkManager", "Adding JogEntry COMPLETE")
            } catch (e: Exception) {
                Log.e(TAG, "Exception: ${e.message}")
                return@withContext Result.failure()
            }
        }

        withContext(Dispatchers.IO) {
            // jogSummaryTemp
            val modifiedTempJogSummary = if (jogSummaryTemp == null) {
                Log.i("JogSummaryWorkManager", "Adding First JogSummary Temp")
                ModifiedTempJogSummary(
                    jogId = id,
                    date = currentDateTime,
                    location = currentLatLng
                )
            } else {
                Log.i("JogSummaryWorkManager", "Updating JogSummary Temp")
                val updatedDistance = jogSummaryTemp!!.totalDistance +
                        getTotalDistance(listOf(jogSummaryTemp!!.location, currentLatLng))
                val updatedDuration =
                    Duration.between(jogSummaryTemp!!.date, currentDateTime)
                ModifiedTempJogSummary(
                    jogId = id,
                    date = currentDateTime,
                    totalDistance = updatedDistance,
                    duration = updatedDuration,
                )
            }
            jogUseCase.addOrUpdateJogSummaryTemp(modifiedTempJogSummary)
            jogSummaryTemp = modifiedTempJogSummary
            Log.i("JogSummaryWorkManager", "Adding JogSummary Temp COMPLETE")

        }

        withContext(Dispatchers.IO) {
            try {
                Log.i("JogSummaryWorkManager", "Adding/Updating JogSummary")
                val modifiedJogSummary = ModifiedJogSummary(
                    jogId = jogSummaryTemp!!.jogId,
                    startDate = jogSummaryTemp!!.date,
                    duration = jogSummaryTemp!!.duration,
                    totalDistance = jogSummaryTemp!!.totalDistance
                )
                jogUseCase.addJogSummary(modifiedJogSummary)
                Log.i("JogSummaryWorkManager", "Adding/Updating JogSummary COMPLETED")
                Result.success()

            } catch (e: Exception) {
                Log.e(this.javaClass.simpleName, "Exception: ${e.message}")
                return@withContext Result.failure()
            }
        }
        return Result.success()
    }
}