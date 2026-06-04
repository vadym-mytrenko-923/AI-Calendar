package com.ai.calendar.demo.utils

import android.os.Build
import com.ai.calendar.demo.BuildConfig

private const val OS_INFO_UNDEFINED = "Undefined"

object AppInfo {
    val flavor: String = BuildConfig.FLAVOR

    val isTestEnv: Boolean
        get() = flavor == "qa" || flavor == "uat"

    val isDebug: Boolean
        get() = BuildConfig.DEBUG

    val appVersion: String
        get() = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"

    val isNotificationPermissionNeeded: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    fun getOsInfo(): String = try {
        "OS version: ${System.getProperty("os.version")}.: ${Build.DISPLAY}"
    } catch (_: Exception) {
        OS_INFO_UNDEFINED
    }
}
