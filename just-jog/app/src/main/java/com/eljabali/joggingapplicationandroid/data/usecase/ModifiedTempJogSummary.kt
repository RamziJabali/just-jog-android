package com.eljabali.joggingapplicationandroid.data.usecase

import com.google.android.gms.maps.model.LatLng
import java.time.ZonedDateTime

data class ModifiedTempJogSummary(
    val jogId: Int = 0,
    val date: ZonedDateTime = ZonedDateTime.now(),
    val totalDistance: Double = 0.0,
    val timeDurationInSeconds: Long = 0,
    val location: LatLng = LatLng(0.0, 0.0)
)
