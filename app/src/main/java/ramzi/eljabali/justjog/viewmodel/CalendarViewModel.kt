package ramzi.eljabali.justjog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javatimefun.zoneddatetime.ZonedDateTimes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ramzi.eljabali.justjog.usecase.JogUseCase
import ramzi.eljabali.justjog.viewstate.CalendarViewState
import java.time.ZonedDateTime

class CalendarViewModel(
    private val jogUseCase: JogUseCase,
) : ViewModel() {
    private val _calendarViewState = MutableStateFlow(CalendarViewState())
    val calendarViewState = _calendarViewState.asStateFlow()

    fun onLaunch() {
        viewModelScope.launch {
            val startDate = ZonedDateTimes.today.withDayOfMonth(1)
            val endDate =
                ZonedDateTimes.today.withDayOfMonth(ZonedDateTimes.today.month.maxLength())
            val monthlyJogSummaries = getMonthlyJogSummaries(startDate, endDate)
            _calendarViewState.update {
                it.copy(
                    currentMonthJogSummaries = monthlyJogSummaries
                )
            }
        }
    }

    private suspend fun getMonthlyJogSummaries(startDate: ZonedDateTime, endDate: ZonedDateTime) =
        viewModelScope.async(Dispatchers.IO) {
            jogUseCase.getJogSummariesBetweenDates(startDate, endDate).first()
        }.await()
}