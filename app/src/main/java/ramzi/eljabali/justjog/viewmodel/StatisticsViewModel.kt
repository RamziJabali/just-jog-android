package ramzi.eljabali.justjog.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eljabali.joggingapplicationandroid.util.getFormattedTimeSeconds
import com.jaikeerthick.composable_graphs.composables.line.model.LineData
import javatimefun.zoneddatetime.ZonedDateTimes
import javatimefun.zoneddatetime.extensions.compareDay
import javatimefun.zoneddatetime.extensions.getLast
import javatimefun.zoneddatetime.extensions.getNext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import ramzi.eljabali.justjog.motivationalquotes.MotivationQuotesAPI
import ramzi.eljabali.justjog.usecase.JogUseCase
import ramzi.eljabali.justjog.usecase.ModifiedJogSummary
import ramzi.eljabali.justjog.util.DurationFormat
import ramzi.eljabali.justjog.util.TAG
import ramzi.eljabali.justjog.viewstate.StatisticsViewState
import java.time.Duration
import java.time.ZonedDateTime
import kotlin.math.min


class StatisticsViewModel(
    private val jogUseCase: JogUseCase,
    private val motivationQuotesAPI: MotivationQuotesAPI
) : ViewModel() {
    private val _statisticsViewState = MutableStateFlow(StatisticsViewState())
    val statisticsViewState: StateFlow<StatisticsViewState> = _statisticsViewState.asStateFlow()

    companion object {
        private val daysOfTheWeek = listOf("Mon", "Tues", "Wed", "Thurs", "Fri", "Sat", "Sun")
    }

    fun onLaunch() {
        viewModelScope.launch {
            val quote = getQuote()
            val startOfWeekDate = ZonedDateTimes.today.getLast(
                DayOfWeek.MONDAY,
                countingInThisDay = true
            )
            val endOfWeekDate =
                ZonedDateTimes.today.getNext(DayOfWeek.SUNDAY, countingInThisDay = true)
            val thisWeeksJogSummaries = getWeeklyJogSummaries(startOfWeekDate, endOfWeekDate)
            val chartStatistics =
                getChartData(thisWeeksJogSummaries, startOfWeekDate, endOfWeekDate)
            val weeklyStatisticsBreakDown =
                getWeeklyStatisticsBreakDown(thisWeeksJogSummaries)
            val jogStatisticsBreakDown = getWeeklyPerJogStatisticsBreakDown(thisWeeksJogSummaries)
            _statisticsViewState.update {
                it.copy(
                    quote = quote,
                    lineDataList = chartStatistics,
                    weeklyStatisticsBreakDown = weeklyStatisticsBreakDown,
                    perJogStatisticsBreakDown =jogStatisticsBreakDown
                )
            }
        }
    }

    private fun getChartData(
        summaryList: List<ModifiedJogSummary>,
        startOfWeek: ZonedDateTime,
        endOfWeek: ZonedDateTime
    ): List<LineData> {
        val listOfLineData = mutableListOf<LineData>()
        var tempDate = startOfWeek
        var dayOfWeek = 0
        var totalMilesInDay = 0.0
        for (summary in summaryList) {
            if (tempDate.compareDay(summary.startDate) != 0) {
                listOfLineData.add(LineData(x = daysOfTheWeek[dayOfWeek], y = totalMilesInDay))
                totalMilesInDay = 0.0
                tempDate = tempDate.plusDays(1)
                dayOfWeek++
            }
            totalMilesInDay += summary.totalDistance
        }
        if (tempDate.compareDay(endOfWeek) != 0) {
            repeat(6 - dayOfWeek) {
                listOfLineData.add(LineData(x = daysOfTheWeek[dayOfWeek], y = 0.0))
                dayOfWeek++
            }
        }
        return listOfLineData
    }

    private fun getWeeklyStatisticsBreakDown(
        weeklyJogSummaries: List<ModifiedJogSummary>
    ): List<String> {
        val totalJogs = "${weeklyJogSummaries.size} Runs"
        var totalMiles = 0.0
        var totalDuration = Duration.ZERO
        for (summary in weeklyJogSummaries) {
            totalMiles += summary.totalDistance
            totalDuration = totalDuration.plus(summary.duration)
        }
        return listOf(
            totalJogs,
            "$totalMiles Miles",
            getFormattedTimeSeconds(totalDuration.seconds, DurationFormat.HMS)
        )
    }

    private fun getWeeklyPerJogStatisticsBreakDown(thisWeeksJogSummaries: List<ModifiedJogSummary>): List<String> {
        val averageMilesPerJog =
            thisWeeksJogSummaries.sumOf { it.totalDistance } / thisWeeksJogSummaries.size
        val averageDurationPerJog =
            thisWeeksJogSummaries.sumOf { it.duration.seconds } / thisWeeksJogSummaries.size
        val minutesPerMile = averageDurationPerJog / averageMilesPerJog
        return listOf(
            "$averageMilesPerJog Miles",
            "$minutesPerMile Mins/Mil",
            getFormattedTimeSeconds(averageDurationPerJog, DurationFormat.MS)
        )
    }

    private suspend fun getWeeklyJogSummaries(
        startOfWeek: ZonedDateTime,
        endOfWeek: ZonedDateTime
    ): List<ModifiedJogSummary> {
        val result = viewModelScope.async {
            jogUseCase.getJogSummariesBetweenDates(
                startOfWeek, endOfWeek
            ).first()
        }.await()
        return result
    }


    private suspend fun getQuote(): String {
        val quote = viewModelScope.async(Dispatchers.IO) {
            var body = ""
            try {
                val result = motivationQuotesAPI.getQuote().body()
                body = if (result != null) {
                    result[0].quote
                } else {
                    ""
                }
            } catch (e: Exception) {
                Log.e(this.TAG, "${e.message}")
            }
            body
        }
        return quote.await()
    }
}