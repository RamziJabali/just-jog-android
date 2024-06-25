package ramzi.eljabali.justjog.usecase

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import javatimefun.zoneddatetime.extensions.print
import javatimefun.zoneddatetime.extensions.toZonedDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import ramzi.eljabali.justjog.motivationalquotes.MotivationQuotesAPI
import ramzi.eljabali.justjog.repository.room.jogentries.JogEntry
import ramzi.eljabali.justjog.repository.room.jogentries.JogEntryDAO
import ramzi.eljabali.justjog.repository.room.jogsummary.JogSummary
import ramzi.eljabali.justjog.repository.room.jogsummary.JogSummaryDAO
import ramzi.eljabali.justjog.repository.room.jogsummarytemp.JogSummaryTemp
import ramzi.eljabali.justjog.repository.room.jogsummarytemp.JogSummaryTempDAO
import ramzi.eljabali.justjog.util.DateFormat
import ramzi.eljabali.justjog.util.TAG
import java.time.Duration
import java.time.ZonedDateTime

class JogUseCase(
    private val jogEntryDao: JogEntryDAO,
    private val jogSummaryDao: JogSummaryDAO,
    private val jogSummaryTempDao: JogSummaryTempDAO,
) {

    // JogEntry
    fun addJogEntry(modifiedJogEntry: ModifiedJogEntry) {
        jogEntryDao.addEntry(
            convertModifiedJogEntryToJogEntry(modifiedJogEntry)
        )
    }

    fun getAllJogEntries() = jogEntryDao.getAll()

    // SUMMARY
    suspend fun getNewJogID(): Int {
        Log.i(TAG, "getNewJogID() -> JogUseCase")
        val id = jogSummaryDao.getLastID().firstOrNull()

        return if (id == null) {
            Log.i(TAG, "getNewJogID() -> 1")
            1
        } else {
            Log.i(TAG, "getNewJogID() -> ${id + 1}")
            id + 1
        }
    }

    fun addJogSummary(modifiedJogSummary: ModifiedJogSummary) {
        jogSummaryDao.add(
            JogSummary(
                id = modifiedJogSummary.jogId,
                startDate = modifiedJogSummary.startDate.print(DateFormat.YYYY_MM_DD_T_TIME.format),
                totalJogDuration = modifiedJogSummary.duration.seconds,
                totalJogDistance = modifiedJogSummary.totalDistance,
            )
        )
    }

    fun getJogSummariesBetweenDates(
        startDate: ZonedDateTime,
        endDate: ZonedDateTime
    ): Flow<List<ModifiedJogSummary>> =
        jogSummaryDao.getBetweenDatesUseCase(startDate, endDate).map { list ->
            list.map {
                ModifiedJogSummary(
                    jogId = it.id,
                    startDate = it.startDate.toZonedDateTime(DateFormat.YYYY_MM_DD_T_TIME.format)!!,
                    duration = Duration.ofSeconds(it.totalJogDuration),
                    totalDistance = it.totalJogDistance
                )
            }
        }


    // SUMMARY TEMP
    fun getJogSummaryTemp(id: Int) = jogSummaryTempDao.getById(id).map { jogSummaryTemp ->
        if (jogSummaryTemp != null) {
            ModifiedTempJogSummary(
                jogId = jogSummaryTemp.id,
                date = jogSummaryTemp.currentDateTime.toZonedDateTime(DateFormat.YYYY_MM_DD_T_TIME.format)!!,
                totalDistance = jogSummaryTemp.totalJogDistance,
                duration = Duration.ofSeconds(jogSummaryTemp.totalJogDuration),
                location = LatLng(jogSummaryTemp.latitude, jogSummaryTemp.longitude)
            )
        } else {
            null
        }
    }

    fun deleteAllTempJogSummaries() = jogSummaryTempDao.deleteAll()
    fun addOrUpdateJogSummaryTemp(modifiedTempJogSummary: ModifiedTempJogSummary) {
        jogSummaryTempDao.add(
            JogSummaryTemp(
                id = modifiedTempJogSummary.jogId,
                currentDateTime = modifiedTempJogSummary.date.print(DateFormat.YYYY_MM_DD_T_TIME.format),
                totalJogDuration = modifiedTempJogSummary.duration.seconds,
                totalJogDistance = modifiedTempJogSummary.totalDistance,
                latitude = modifiedTempJogSummary.location.latitude,
                longitude = modifiedTempJogSummary.location.longitude
            )
        )
    }


    private fun convertModifiedJogEntryToJogEntry(modifiedJogDateInformation: ModifiedJogEntry): JogEntry =
        JogEntry(
            id = 0,
            jogSummaryId = modifiedJogDateInformation.jogSummaryId,
            dateTime = modifiedJogDateInformation.dateTime.print(DateFormat.YYYY_MM_DD_T_TIME.format),
            latitude = modifiedJogDateInformation.latLng.latitude,
            longitude = modifiedJogDateInformation.latLng.longitude
        )
}

