package com.eljabali.joggingapplicationandroid.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object PermissionUtil {

    fun isGpsLocationGranted(context: Context): Boolean =
        isPermissionGranted(context, Manifest.permission.ACCESS_COARSE_LOCATION) &&
        isPermissionGranted(context, Manifest.permission.ACCESS_FINE_LOCATION)

    fun isPermissionGranted(context: Context, id: String): Boolean =
        ActivityCompat.checkSelfPermission(context, id) == PackageManager.PERMISSION_GRANTED
}