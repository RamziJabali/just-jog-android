package ramzi.eljabali.justjog.viewstate

import ramzi.eljabali.justjog.usecase.ModifiedJogSummary

data class CalendarViewState(
    val currentMonthJogSummaries: List<ModifiedJogSummary> = emptyList(),
    val jogsInSelectedDay: List<ModifiedJogSummary> = emptyList(),
    val shouldShowJogSummaries: Boolean = false
)
