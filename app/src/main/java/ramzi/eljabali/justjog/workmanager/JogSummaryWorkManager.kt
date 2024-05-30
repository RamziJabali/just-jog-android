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
    override suspend fun doWork(): Result {
        val currentLatLng = LatLng(
            inputData.getDouble("KEY_LATITUDE", 0.0),
            inputData.getDouble("KEY_LONGITUDE", 0.0)
        )
        val id = inputData.getInt("ID", -1)
        val currentDateTime: ZonedDateTime = inputData.getString("DATE_TIME")
            ?.toZonedDateTime(DateFormat.YYYY_MM_DD_T_TIME.format)!!

        var jogSummaryTemp: ModifiedTempJogSummary? = null

        withContext(Dispatchers.IO) async@{
            try {
                jogSummaryTemp = async {
                    jogUseCase.getJogSummaryTemp(id)
                        .onStart {
                            Log.i("JogSummaryWorkManager", "Before GetTempJogSummary")
                        }
                        .onCompletion {
                            Log.i("JogSummaryWorkManager", "After GetTempJogSummary")
                        }.firstOrNull()
                }.await()
            } catch (e: Exception) {
                Log.e(TAG, "Exception: ${e.message}")
                return@async Result.failure()
            }
            try {
                async {

                    Log.i("JogSummaryWorkManager", "Adding JogEntry")
                    jogUseCase.addJogEntry(
                        ModifiedJogEntry(
                            jogSummaryId = id,
                            dateTime = currentDateTime,
                            latLng = currentLatLng
                        )
                    )
                    Log.i("JogSummaryWorkManager", "Adding JogEntry COMPLETE")
                }.await()
            } catch (e: Exception) {
                Log.e(TAG, "Exception: ${e.message}")
                return@async Result.failure()
            }

            try {
                async {
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
                }.await()
            } catch (e: Exception) {
                Log.e(this.javaClass.simpleName, "Exception: ${e.message}")
                return@async Result.failure()
            }

            try {
                async {
                    Log.i("JogSummaryWorkManager", "Adding/Updating JogSummary")
                    val modifiedJogSummary = ModifiedJogSummary(
                        jogId = jogSummaryTemp!!.jogId,
                        startDate = jogSummaryTemp!!.date,
                        duration = jogSummaryTemp!!.duration,
                        totalDistance = jogSummaryTemp!!.totalDistance
                    )
                    jogUseCase.addJogSummary(modifiedJogSummary)
                    Log.i("JogSummaryWorkManager", "Adding/Updating JogSummary COMPLETED")
                }.await()
            } catch (e: Exception) {
                Log.e(this.javaClass.simpleName, "Exception: ${e.message}")
                return@async Result.failure()
            }
        }
        return Result.success()
    }
}