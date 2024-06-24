package ramzi.eljabali.justjog.viewstate

import com.jaikeerthick.composable_graphs.composables.line.model.LineData

data class StatisticsViewState(
    val quote: String = "",
    val lineDataList: List<LineData> = listOf(
        LineData(x = "Mon", y = 0),
        LineData(x = "Tues", y = 0),
        LineData(x = "Wed", y = 0),
        LineData(x = "Thurs", y = 0),
        LineData(x = "Fri", y = 0),
        LineData(x = "Sat", y = 0),
        LineData(x = "Sun", y = 0),
    ),
    val weeklyStatisticsBreakDown: List<String> = listOf("", "", ""),
    val perJogStatisticsBreakDown: List<String> = listOf("", "", ""),
    val shouldBlur: Boolean = false,
)