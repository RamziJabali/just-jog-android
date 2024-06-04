package ramzi.eljabali.justjog.viewstate

import com.jaikeerthick.composable_graphs.composables.line.model.LineData

data class StatisticsViewState(
    val quote: String = "",
    val lineDataList: List<LineData> = emptyList(),
    val weeklyStatisticsBreakDown: List<String> = emptyList(),
    val perJogStatisticsBreakDown: List<String> = emptyList()
)