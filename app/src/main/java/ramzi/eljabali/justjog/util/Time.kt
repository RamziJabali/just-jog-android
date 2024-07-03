package ramzi.eljabali.justjog.util

import java.util.Locale

fun hoursToMinutes(hours: Long): Long = hours * 60
fun hoursToSeconds(hours: Long): Long = hours * 3600
fun minutesToSeconds(minutes: Long): Long = minutes * 60
fun minutesToHours(minutes: Long): Long = minutes / 60
fun secondsToMinutes(seconds: Long): Long = seconds / 60
fun secondsToHours(seconds: Long): Long = seconds / 3600
fun secondsToHoursDouble(seconds: Long): Double = seconds / 3600.0

fun getFormattedTime(totalTimeInSeconds: Long, format: DurationFormat): String {
    var tempTime = totalTimeInSeconds
    val totalHours = secondsToHours(tempTime)
    tempTime -= hoursToSeconds(totalHours)
    val totalMinutes = secondsToMinutes(tempTime)
    tempTime -= minutesToSeconds(totalMinutes)
    val totalSeconds = tempTime
    if (format == DurationFormat.HH_MM_SS) {
        return String.format(Locale.US, "%02d:%02d:%02d", totalHours, totalMinutes, totalSeconds)
    } else if (format == DurationFormat.H_M_S) {
        return if (totalHours.compareTo(0) == 0) {
            String.format(Locale.US, "%01dm %01ds", totalMinutes, totalSeconds)
        } else {
            String.format(Locale.US, "%01dh %01dm %01ds", totalHours, totalMinutes, totalSeconds)
        }
    }
    return String.format(Locale.US, "%01dh %01dm %01ds", totalHours, totalMinutes, totalSeconds)
}


fun getFormattedTimeSeconds(totalTimeInSeconds: Long, format: DurationFormat = DurationFormat.HH_MM_SS): String {
    var tempTime = totalTimeInSeconds
    val totalHours = secondsToHours(tempTime)
    tempTime %= 3600
    val totalMinutes = secondsToMinutes(tempTime)
    tempTime %= 60
    val totalSeconds = tempTime

    return when (format) {
        DurationFormat.HH_MM_SS -> String.format(Locale.US,"%02d:%02d:%02d", totalHours, totalMinutes, totalSeconds)
        DurationFormat.H_M_S -> String.format(Locale.US,"%d:%d:%d", totalHours, totalMinutes, totalSeconds)
        DurationFormat.HMS -> String.format(Locale.US, "%02dh %02dm %02ds", totalHours, totalMinutes, totalSeconds)
        DurationFormat.MS -> String.format(Locale.US, "%01dm %02ds", totalHours, totalMinutes, totalSeconds)
    }
}