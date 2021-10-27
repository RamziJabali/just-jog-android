package com.eljabali.joggingapplicationandroid.data.usecase

import java.time.ZonedDateTime

data class ModifiedJogSummary(
    val jogId: Int = 0,
    val date: ZonedDateTime = ZonedDateTime.now()
)