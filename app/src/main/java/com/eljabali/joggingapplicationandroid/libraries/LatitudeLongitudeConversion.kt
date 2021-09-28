package com.eljabali.joggingapplicationandroid.libraries

import com.google.android.gms.maps.model.LatLng
import kotlin.math.cos
import kotlin.math.sin

fun getTotalDistance(listOfPoints: List<LatLng>): Double {
    var totalDistance = 0.0
    if (listOfPoints.size >= 2) {
        var index = 1
        while (index < listOfPoints.size) {
            totalDistance += getDistanceBetweenTwoLatLngToMiles(
                listOfPoints[index - 1],
                listOfPoints[index]
            )
            index++
        }
        return String.format("%.3f", totalDistance).toDouble()
    }
    return totalDistance
}



private fun getDistanceBetweenTwoLatLngToMiles(point1: LatLng, point2: LatLng): Double {
    val earthsRadius = 3958.8
    val dLat = degreesToRadian(point1.latitude - point2.latitude)
    val dLon = degreesToRadian(point1.longitude - point2.longitude)
    val a =
        sin(dLat / 2) * sin(dLat / 2) +
                cos(degreesToRadian(point1.latitude)) * cos(degreesToRadian(point2.latitude)) *
                sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
    val final = earthsRadius * c

    return String.format("%.3f", final).toDouble()
}


private fun degreesToRadian(degree: Double): Double {
    return (degree * (Math.PI / 180))
}