package ramzi.eljabali.justjog.ui.views

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.LocalDate
import ramzi.eljabali.justjog.R
import ramzi.eljabali.justjog.ui.design.CardElevation
import ramzi.eljabali.justjog.ui.design.Spacing
import ramzi.eljabali.justjog.ui.design.Typography
import ramzi.eljabali.justjog.ui.design.secondaryTextColor
import ramzi.eljabali.justjog.ui.design.white
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.log

@Composable
fun CalendarPage() {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(0) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(0) } // Adjust as needed
    val daysOfWeek = remember { daysOfWeek() }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.Horizontal.s, vertical = Spacing.Vertical.m),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Spacing.Vertical.s),
            elevation = CardDefaults.cardElevation(
                defaultElevation = CardElevation.default
            )
        ) {
            HorizontalCalendar(
                state = state,
                dayContent = {
                    Day(it) { day ->
                        Log.i("Date Clicked", "Date {${day.date}} has been clicked")
                    }
                },
                monthHeader = { month ->
                    val daysOfWeek = month.weekDays.first().map { it.date.dayOfWeek }
                    MonthHeader(daysOfWeek = daysOfWeek, month, state)
                }
            )
        }
    }
}

@Composable
fun MonthHeader(
    daysOfWeek: List<DayOfWeek>,
    month: CalendarMonth,
    state: CalendarState
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                Log.i("Button Click", "Back Button clicked in month header")
            }
            ) {
                Icon(
                    painter = painterResource(R.mipmap.back_arrrow_foreground),
                    contentDescription = "Back icon"
                )
            }
            Text(
                text = "${month.yearMonth.month}",
                modifier = Modifier.padding(Spacing.Vertical.s),
                style = Typography.titleMedium
            )
            IconButton(onClick = {
                Log.i(
                    "Button Click",
                    "front Button clicked in month header"
                )
            }) {
                Icon(
                    painter = painterResource(R.mipmap.front_arrow_foreground),
                    contentDescription = "front icon"
                )
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            for (dayOfWeek in daysOfWeek) {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                )
            }
        }
    }
}

@Composable
fun Day(day: CalendarDay, onClick: (CalendarDay) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(.9f)// This is important for square sizing!
            .clickable(
                enabled = true,
                onClick = { onClick(day) }
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = if (day.position == DayPosition.MonthDate) white else secondaryTextColor
        )
    }
}

/*
TODO:
    - Add parameters
    - Fix View - make it able to be populated
    - Should be clickable
    - Show showcase another view
*/
@Composable
fun JogView() {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.Vertical.s),
        elevation = CardDefaults.cardElevation(
            defaultElevation = CardElevation.default
        )
    ) {

    }
}

@Preview(showBackground = true, backgroundColor = 1)
@Composable
fun PreviewCalendarPage() {
    CalendarPage()
//    JogView()
}