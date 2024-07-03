package ramzi.eljabali.justjog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javatimefun.zoneddatetime.ZonedDateTimes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import ramzi.eljabali.justjog.usecase.JogUseCase
import java.time.ZonedDateTime

class CalendarViewModel(
    private val jogUseCase: JogUseCase,
) : ViewModel() {
    fun onLaunch() {
        viewModelScope.launch(Dispatchers.IO) {
            val startDate = ZonedDateTimes.today.withDayOfMonth(1)
            val endDate =
                ZonedDateTimes.today.withDayOfMonth(ZonedDateTimes.today.month.maxLength())
            val monthlyJogSummaries = getMonthlyJogSummaries(startDate, endDate)

        }
    }

    private suspend fun getMonthlyJogSummaries(startDate: ZonedDateTime, endDate: ZonedDateTime) =
        viewModelScope.async {
            jogUseCase.getJogSummariesBetweenDates(startDate, endDate)
        }.await().single()
}