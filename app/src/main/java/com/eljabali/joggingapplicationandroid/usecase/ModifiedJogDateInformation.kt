package com.eljabali.joggingapplicationandroid.usecase

import com.google.android.gms.maps.model.LatLng
import zoneddatetime.ZonedDateTimeUtil
import zoneddatetime.ZonedDateTimes
import java.time.ZonedDateTime

data class ModifiedJogDateInformation(
    val dateTime: ZonedDateTime,
    val runNumber: Int,
    val latitudeLongitude: LatLng,
)