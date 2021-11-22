package com.eljabali.joggingapplicationandroid.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.IntRange
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object PermissionUtil {
    /**
     *  Has Permission
     */
    fun isGpsLocationGranted(context: Context): Boolean =
        isGranted(context, Manifest.permission.ACCESS_COARSE_LOCATION) &&
                isGranted(context, Manifest.permission.ACCESS_FINE_LOCATION)

    @JvmStatic
    fun isGranted(context: Context, permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    @JvmStatic
    fun isNotGranted(context: Context, permission: String): Boolean =
        !isGranted(context, permission)

    /**
     *  Request Permission
     */
    @JvmStatic
    fun request(
        activity: Activity,
        permission: String,
        @IntRange(from = 0, to = 65535) requestCode: Int
    ) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
    }

    @JvmStatic
    fun request(
        activity: Activity,
        permissions: Array<String>,
        @IntRange(from = 0, to = 65535) requestCode: Int
    ) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }

    @JvmStatic
    fun request(
        fragment: Fragment,
        permission: String,
        @IntRange(from = 0, to = 65535) requestCode: Int
    ) {
        fragment.requestPermissions(arrayOf(permission), requestCode)
    }

    @JvmStatic
    fun request(
        fragment: Fragment,
        permissions: Array<String>,
        @IntRange(from = 0, to = 65535) requestCode: Int
    ) {
        fragment.requestPermissions(permissions, requestCode)
    }

    /**
     *  Granted Permission
     */
    @JvmStatic
    fun wasGranted(grantResult: Int): Boolean = grantResult == PackageManager.PERMISSION_GRANTED

    @JvmStatic
    fun wasGranted(grantResults: IntArray): Boolean = wasGranted(grantResults[0])
}