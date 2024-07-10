package ramzi.eljabali.justjog.ui.views

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.mapbox.maps.MapView
import javatimefun.localdate.LocalDates.today
import javatimefun.zoneddatetime.ZonedDateTimes
import javatimefun.zoneddatetime.extensions.print
import kotlinx.coroutines.flow.MutableStateFlow
import ramzi.eljabali.justjog.ui.design.ButtonSize
import ramzi.eljabali.justjog.ui.design.CardSize
import ramzi.eljabali.justjog.ui.design.Spacing
import ramzi.eljabali.justjog.ui.design.Spacing.Vertical.xs
import ramzi.eljabali.justjog.ui.design.errorContainerDark
import ramzi.eljabali.justjog.ui.design.lightBlue
import ramzi.eljabali.justjog.ui.util.MapsScreen
import ramzi.eljabali.justjog.usecase.ModifiedJogSummary
import ramzi.eljabali.justjog.util.DateFormat
import ramzi.eljabali.justjog.util.getDayOfTheWeekTheMonthStartsIn
import ramzi.eljabali.justjog.util.getFormattedTimeSeconds
import ramzi.eljabali.justjog.util.getNumberOfWeeksInMonth
import ramzi.eljabali.justjog.viewstate.CalendarViewState
import java.time.Duration
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.Locale

@Composable
fun JustJogCalendarView(
    jogCalendarViewState: State<CalendarViewState>,
    getJogSummariesForDate: (ZonedDateTime) -> Unit,
    showJogSummariesAtDate: (List<ModifiedJogSummary>) -> Unit,
    navController: NavHostController
) {
    var date by remember {
        mutableStateOf(ZonedDateTimes.today)
    }
    var monthHeader by remember {
        mutableStateOf("${date.month.name} ${date.year}")
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.Surrounding.s)
    ) {
        ElevatedCard {
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
                        getJogSummariesForDate(date)
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
                        getJogSummariesForDate(date)
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
                Month(
                    monthDate = date.toLocalDate(),
                    monthJogs = jogCalendarViewState.value.currentMonthJogSummaries,
                    showJogSummariesAtDate
                )
            }
        }
        if (jogCalendarViewState.value.shouldShowJogSummaries) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.Vertical.s)
            ) {
                items(jogCalendarViewState.value.jogsInSelectedDay.size) { index ->
                    ElevatedCard(
                        modifier = Modifier
                            .size(CardSize.xl4, CardSize.xxl)
                            .padding(end = Spacing.Horizontal.s)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val stringDate =
                                jogCalendarViewState.value.jogsInSelectedDay[index].startDate.print(
                                    DateFormat.YYYY_MM_DD.format,
                                    Locale.US
                                )
                            Text(
                                modifier = Modifier.padding(
                                    top = xs,
                                    bottom = xs
                                ), text = stringDate
                            )
                            Text(
                                modifier = Modifier.padding(
                                    top = xs,
                                    bottom = xs
                                ), text = "Jog #${index + 1}"
                            )
                            Text(
                                modifier = Modifier.padding(
                                    top = xs,
                                    bottom = xs
                                ),

                                text = String.format(
                                    Locale.US,
                                    "Total Distance: %.2f Miles",
                                    jogCalendarViewState.value.jogsInSelectedDay[index].totalDistance
                                )
                            )
                            Text(
                                modifier = Modifier.padding(
                                    top = xs,
                                    bottom = xs
                                ),
                                text = "Total Time: ${getFormattedTimeSeconds(jogCalendarViewState.value.jogsInSelectedDay[index].duration.seconds)} "
                            )
                            Button(
                                modifier = Modifier.padding(
                                    top = xs,
                                    bottom = xs
                                ),
                                onClick = {
                                    navController.navigate(MapsScreen(jogId = jogCalendarViewState.value.jogsInSelectedDay[index].jogId))
                                }) {
                                Text("Map")
                            }
                        }
                    }
                }
            }
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
fun Month(
    monthDate: LocalDate,
    monthJogs: List<ModifiedJogSummary>,
    showJogSummariesAtDate: (List<ModifiedJogSummary>) -> Unit
) {
    val monthLength = monthDate.lengthOfMonth()
    var dayOfWeekTheMonthStartsOn =
        getDayOfTheWeekTheMonthStartsIn(monthDate) - 1 // mondqy = 1, sunday = 7
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
                endOfWeek = date.plusDays(6 - dayOfWeekTheMonthStartsOn.toLong()),
                monthJogs = monthJogs,
                showJogSummariesAtDate
            )
            date = date.plusDays(7 - dayOfWeekTheMonthStartsOn.toLong())
            dayOfWeekTheMonthStartsOn = 0
        }
        if ((monthLength + dayOfTheWeek) % 7 != 0) {
            Week(
                startOfWeek = date,
                endOfWeek = date.plusDays((monthLength - date.dayOfMonth).toLong()),
                monthJogs = monthJogs,
                showJogSummariesAtDate
            )
        }
    }
}

@Composable
fun Week(
    startOfWeek: LocalDate,
    endOfWeek: LocalDate,
    monthJogs: List<ModifiedJogSummary>,
    showJogSummariesAtDate: (List<ModifiedJogSummary>) -> Unit
) {
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
            Day(
                date,
                monthJogs.any { it.startDate.dayOfMonth == date.dayOfMonth },
                monthJogs.filter { jog ->
                    jog.startDate.toLocalDate().compareTo(date) == 0
                },
                showJogSummariesAtDate
            )
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
fun Day(
    date: LocalDate,
    didUserJog: Boolean,
    jogSummariesAtDate: List<ModifiedJogSummary>,
    showJogSummariesAtDate: (List<ModifiedJogSummary>) -> Unit
) {
    Box(
        modifier = Modifier
            .size(ButtonSize.m)
            .clickable(
                enabled = didUserJog,
                onClick = {
                    Log.d("JustJog-CalendarView", "Clicked on $date")
                    showJogSummariesAtDate(jogSummariesAtDate)
                }
            )
            .background(if (didUserJog) lightBlue else errorContainerDark),
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
    val calendarViewState = MutableStateFlow(CalendarViewState())
//    JustJogCalendarView(calendarViewState.collectAsState(), listOf(ModifiedJogSummary))
}

@Preview(backgroundColor = 0xFFF0EAE2)
@Composable
fun PreviewJustJogDayViewFalse() {
//    Day(
//        today, false,
//    )
}

@Preview(backgroundColor = 0xFFF0EAE2)
@Composable
fun PreviewJustJogDayViewTrue() {
//    Day(
//        today, true, monthJogs.filter { jog ->
//            jog.startDate.toLocalDate().compareTo(date) == 0
//        }
//    ) { }
}

@Preview(backgroundColor = 0xFFF0EAE2)
@Composable
fun PreviewJustJogWeekView() {
    val listOfJogs = listOf(
        ModifiedJogSummary(
            jogId = 0,
            startDate = ZonedDateTime.now(),
            duration = Duration.of(20, ChronoUnit.MINUTES),
            totalDistance = 3.5
        ),
        ModifiedJogSummary(
            jogId = 1,
            startDate = ZonedDateTime.now().plusDays(2),
            duration = Duration.of(25, ChronoUnit.MINUTES),
            totalDistance = 7.0
        )
    )
    Week(
        startOfWeek = today.minusDays(3),
        endOfWeek = today.plusDays(3),
        monthJogs = listOfJogs,
        showJogSummariesAtDate = { }
    )
}