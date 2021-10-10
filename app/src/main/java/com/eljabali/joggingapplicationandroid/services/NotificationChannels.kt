package com.eljabali.joggingapplicationandroid.services

import android.app.NotificationManager
import androidx.annotation.StringRes
import com.eljabali.joggingapplicationandroid.R

enum class NotificationChannels(
    val channelId: String,
    @StringRes val channelName: Int,
    @StringRes val channelDescription: Int,
    val channelImportance: Int
) {
    ACTIVE_RUN(
        "ACTIVE-RUN",
        channelName = R.string.active_run,
        channelDescription = R.string.active_run_desc,
        channelImportance = NotificationManager.IMPORTANCE_LOW
    )
}