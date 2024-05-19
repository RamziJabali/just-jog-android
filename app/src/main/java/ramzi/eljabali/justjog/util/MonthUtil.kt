package ramzi.eljabali.justjog.util

import java.time.LocalDate


fun getDayOfTheWeekTheMonthStartsIn(date: LocalDate) =
    kotlinx.datetime.LocalDate(date.year, date.month, 1).dayOfWeek.value

fun getNumberOfWeeksInMonth(monthLength: Int)= (monthLength) / 7
