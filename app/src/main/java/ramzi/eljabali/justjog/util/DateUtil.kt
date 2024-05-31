package ramzi.eljabali.justjog.util

import java.time.Duration
import java.time.LocalDate
import kotlin.math.absoluteValue


fun getDayOfTheWeekTheMonthStartsIn(date: LocalDate) =
    kotlinx.datetime.LocalDate(date.year, date.month, 1).dayOfWeek.value

fun getNumberOfWeeksInMonth(monthLength: Int) = (monthLength) / 7

fun formatDuration(duration: Duration): String {
    val hours = duration.toHours()
    val minutes = (duration.toMinutes() % 60)
    val seconds = (duration.seconds % 60)

    return String.format("%01d:%02d:%02d", hours.absoluteValue, minutes.absoluteValue, seconds.absoluteValue)
}