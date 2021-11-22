package com.eljabali.joggingapplicationandroid.util

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
        return String.format("%02d:%02d:%02d", totalHours, totalMinutes, totalSeconds)
    } else if (format == DurationFormat.H_M_S) {
        return if (totalHours.compareTo(0) == 0) {
            String.format("%01dm %01ds", totalMinutes, totalSeconds)
        } else {
            String.format("%01dh %01dm %01ds", totalHours, totalMinutes, totalSeconds)
        }
    }
    return String.format("%01dh %01dm %01ds", totalHours, totalMinutes, totalSeconds)
}


fun getFormattedTimeMinutes(totalTimeInMinutes: Long): String {
    var tempTime = minutesToSeconds(totalTimeInMinutes)
    val totalHours = secondsToHours(tempTime)
    tempTime -= hoursToSeconds(totalHours)
    val totalMinutes = secondsToMinutes(tempTime)
    tempTime -= minutesToSeconds(totalMinutes)
    val totalSeconds = tempTime
    return String.format("%02d:%02d:%02d", totalHours, totalMinutes, totalSeconds)
}