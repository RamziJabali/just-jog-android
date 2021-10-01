package com.eljabali.joggingapplicationandroid.recyclerviewmodel

import java.time.ZonedDateTime

data class RecyclerViewState(
    val launchInformation: RunDateAndID = RunDateAndID(ZonedDateTime.now(), -1)
)

data class RunDateAndID(
    val date: ZonedDateTime,
    val runID: Int
)