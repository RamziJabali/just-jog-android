package com.eljabali.joggingapplicationandroid.libraries

fun hoursToMinutes(hours: Long): Long = hours * 60
fun hoursToSeconds(hours: Long): Long = hours * 3600
fun minutesToSeconds(minutes: Long): Long = minutes * 60
fun minutesToHours(minutes: Long): Long = minutes / 60
fun secondsToMinutes(seconds: Long): Long = seconds / 60
fun secondsToHours(seconds: Long): Long = seconds / 3600

fun getFormattedTime(totalTimeInSeconds: Long): String {
    var tempTime = totalTimeInSeconds
    val totalHours = secondsToHours(tempTime)
    tempTime -= hoursToSeconds(totalHours)
    val totalMinutes = secondsToMinutes(tempTime)
    tempTime -= minutesToSeconds(totalMinutes)
    val totalSeconds = tempTime
    return String.format("%02d:%02d:%02d", totalHours,totalMinutes,totalSeconds)
}