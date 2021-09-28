package com.eljabali.joggingapplicationandroid.libraries

import kotlin.math.abs

data class Time(
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0,
)

fun hoursToMinutes(hours: Int): Int = hours * 60
fun hoursToSeconds(hours: Int): Int = hours * 3600
fun minutesToSeconds(minutes: Int): Int = minutes * 60
fun minutesToHours(minutes: Int): Int = minutes / 60
fun secondsToMinutes(seconds: Int): Int = seconds / 60
fun secondsToHours(seconds: Int): Int = seconds / 3600

fun getTotalTime(listOfTime: List<Time>): String {
    var totalTimeInSeconds: Int =
        hoursToSeconds(listOfTime[0].hours) + minutesToSeconds(listOfTime[0].minutes) + listOfTime[0].seconds
    var index = 1
    while (index < listOfTime.size) {
        totalTimeInSeconds -= hoursToSeconds(listOfTime[index].hours) + minutesToSeconds(listOfTime[index].minutes) + listOfTime[index].seconds
        totalTimeInSeconds = abs(totalTimeInSeconds)
        index += 1
    }
    val totalHours: Int = secondsToHours(totalTimeInSeconds)
    val totalMinutes: Int = secondsToMinutes(totalTimeInSeconds)
    val totalSeconds: Int =
        totalTimeInSeconds - (hoursToSeconds(totalHours) + minutesToSeconds(totalMinutes))
    return "${totalHours}:${totalMinutes}:${totalSeconds}"
}