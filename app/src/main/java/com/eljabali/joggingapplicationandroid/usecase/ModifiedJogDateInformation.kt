package com.eljabali.joggingapplicationandroid.usecase

import com.google.android.gms.maps.model.LatLng
import zoneddatetime.ZonedDateTimeUtil
import zoneddatetime.ZonedDateTimes
import java.time.ZonedDateTime

data class ModifiedJogDateInformation(
    val date: ZonedDateTime,
    val runNumber: Int,
    val latitudeLongitude: LatLng,
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0,
)