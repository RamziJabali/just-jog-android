package com.eljabali.joggingapplicationandroid.data.usecase

import java.time.Duration
import java.time.ZonedDateTime

data class ModifiedJogSummary(
    val jogId: Int = 0,
    val date: ZonedDateTime = ZonedDateTime.now(),
    val totalDistance: Double = 0.0,
    val timeDurationInSeconds: Long = 0
)