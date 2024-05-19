package ramzi.eljabali.justjog.ui.views

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import javatimefun.localdate.LocalDates.today
import ramzi.eljabali.justjog.ui.design.ButtonSize
import ramzi.eljabali.justjog.ui.design.Spacing
import ramzi.eljabali.justjog.util.getDayOfTheWeekTheMonthStartsIn
import ramzi.eljabali.justjog.util.getNumberOfWeeksInMonth
import java.time.LocalDate

@Composable
fun JustJogCalendarView() {
    var date by remember {
        mutableStateOf(today)
    }
    var monthHeader by remember {
        mutableStateOf("${date.month.name} ${date.year}")
    }
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Surrounding.s),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    date = date.minusMonths(1)
                    monthHeader = "${date.month.name} ${date.year}"
                }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = "LeftArrow"
                    )
                }
                MonthHeader(monthHeader)
                IconButton(onClick = {
                    date = date.plusMonths(1)
                    monthHeader = "${date.month.name} ${date.year}"
                }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "RightArrow"
                    )
                }
            }
            WeekHeader()
            Month(monthDate = date)
        }
    }
}

@Composable
fun MonthHeader(monthHeader: String) {
    Text(text = monthHeader)
}

@Composable
fun WeekHeader() {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.Surrounding.xs)
    ) {
        listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach {
            DayOfTheWeek(dayOfTheWeek = it)
        }
    }
}

@Composable
fun Month(monthDate: LocalDate) {
    val monthLength = monthDate.lengthOfMonth()
    var dayOfWeekTheMonthStartsOn = getDayOfTheWeekTheMonthStartsIn(monthDate) - 1
    val dayOfTheWeek = dayOfWeekTheMonthStartsOn
    val numberOfWeeks = getNumberOfWeeksInMonth(monthLength + dayOfWeekTheMonthStartsOn)
    var date = monthDate.withDayOfMonth(1)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.Surrounding.xs)
    ) {
        repeat(numberOfWeeks) {
            Week(
                startOfWeek = date,
                endOfWeek = date.plusDays(6 - dayOfWeekTheMonthStartsOn.toLong())
            )
            date = date.plusDays(7 - dayOfWeekTheMonthStartsOn.toLong())
            dayOfWeekTheMonthStartsOn = 0
        }
        if ((monthLength + dayOfTheWeek) % 7 != 0) {
            Week(
                startOfWeek = date,
                endOfWeek = date.plusDays((monthLength - date.dayOfMonth).toLong())
            )
        }
    }
}

@Composable
fun Week(startOfWeek: LocalDate, endOfWeek: LocalDate) {
    var date = startOfWeek
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.Surrounding.xs)
    ) {
        repeat(startOfWeek.dayOfWeek.value - 1) {
            EmptyDay()
        }
        for (day in startOfWeek.dayOfMonth..endOfWeek.dayOfMonth) {
            Day(date)
            date = date.plusDays(1)
        }
        if (endOfWeek.dayOfWeek.value != 7) {
            repeat(7 - endOfWeek.dayOfWeek.value) {
                EmptyDay()
            }
        }
    }
}

@Composable
fun Day(date: LocalDate) {
    Box(
        modifier = Modifier
            .size(ButtonSize.m)
            .clickable(
                enabled = true,
                onClick = { Log.d("JustJog", "Clicked on $date") }
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = date.dayOfMonth.toString(),
        )
    }
}

@Composable
fun EmptyDay() {
    Box(
        modifier = Modifier
            .size(ButtonSize.m),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "",
        )
    }
}

@Composable
fun DayOfTheWeek(dayOfTheWeek: String) {
    Box(
        modifier = Modifier
            .size(height = ButtonSize.xs, width = ButtonSize.m),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = dayOfTheWeek,
        )
    }
}


@Preview(backgroundColor = 0xFFF0EAE2)
@Composable
fun PreviewJustJogCalendarView() {
    JustJogCalendarView()
}

@Preview(backgroundColor = 0xFFF0EAE2)
@Composable
fun PreviewJustJogDayView() {
    Day(today)
}

@Preview(backgroundColor = 0xFFF0EAE2)
@Composable
fun PreviewJustJogWeekView() {
    Week(startOfWeek = today, endOfWeek = today.plusDays(6))
}