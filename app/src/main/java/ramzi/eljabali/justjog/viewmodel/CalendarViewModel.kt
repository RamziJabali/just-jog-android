package ramzi.eljabali.justjog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ramzi.eljabali.justjog.usecase.JogUseCase
import ramzi.eljabali.justjog.usecase.ModifiedJogSummary
import ramzi.eljabali.justjog.viewstate.CalendarViewState
import java.time.ZonedDateTime

class CalendarViewModel(
    private val jogUseCase: JogUseCase,
) : ViewModel() {
    private val _calendarViewState = MutableStateFlow(CalendarViewState())
    val calendarViewState = _calendarViewState.asStateFlow()

    fun onLaunch() {
        viewModelScope.launch {
            val monthlyJogSummaries = getMonthlyJogSummaries(ZonedDateTime.now())
            _calendarViewState.update {
                it.copy(
                    currentMonthJogSummaries = monthlyJogSummaries
                )
            }
        }
    }

    private suspend fun getMonthlyJogSummaries(date: ZonedDateTime) =
        viewModelScope.async(Dispatchers.IO) {
            val startOfMonthDate = date.withDayOfMonth(1)
            val endOfMonthDate = date.withDayOfMonth(date.month.maxLength())
            jogUseCase.getJogSummariesBetweenDates(startOfMonthDate, endOfMonthDate).first()
        }.await()


    fun getJogSummariesForMonth(date: ZonedDateTime) {
        viewModelScope.launch {
            val jogs = getMonthlyJogSummaries(date)
            _calendarViewState.update {
                it.copy(
                    currentMonthJogSummaries = jogs
                )
            }
        }
    }

    fun showJogSummariesAtDate(jogsInDay: List<ModifiedJogSummary>) {
        _calendarViewState.update {
            it.copy(
                jogsInSelectedDay = jogsInDay,
                shouldShowJogSummaries = true
            )
        }
    }
}