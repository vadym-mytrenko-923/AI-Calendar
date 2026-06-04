package com.ai.calendar.demo.ui.core.composable.other

import android.app.Activity
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

interface SystemBarController {
    fun setStatusBarIcons(isWhiteIcons: Boolean)
    fun setNavigationBarAppearance(isLight: Boolean)
}

private class AndroidSystemBarController(
    private val activity: Activity,
    private val viewProvider: () -> View,
) : SystemBarController {
    override fun setStatusBarIcons(isWhiteIcons: Boolean) {
        val controller = WindowCompat.getInsetsController(activity.window, viewProvider())
        controller.isAppearanceLightStatusBars = !isWhiteIcons
    }

    override fun setNavigationBarAppearance(isLight: Boolean) {
        val controller = WindowCompat.getInsetsController(activity.window, viewProvider())
        controller.isAppearanceLightNavigationBars = isLight
    }
}

private val LocalSystemBar = staticCompositionLocalOf<SystemBarController> {
    error("No SystemBarController provided")
}

@Composable
fun SystemBarProvider(content: @Composable () -> Unit) {
    val view = LocalView.current
    val activity = (view.context as? Activity)

    val controller = if (activity != null && !view.isInEditMode) {
        AndroidSystemBarController(activity) { view }
    } else {
        object : SystemBarController {
            override fun setStatusBarIcons(isWhiteIcons: Boolean) = Unit
            override fun setNavigationBarAppearance(isLight: Boolean) = Unit
        }
    }

    CompositionLocalProvider(LocalSystemBar provides controller) {
        content()
    }
}

@Composable
fun SetStatusBarIconsAppearance(isWhiteIcons: Boolean = true) {
    val systemBar = LocalSystemBar.current
    SideEffect {
        systemBar.setStatusBarIcons(isWhiteIcons = isWhiteIcons)
    }
}

@Composable
fun SetNavigationBarAppearance(isLight: Boolean = true) {
    val systemBar = LocalSystemBar.current
    SideEffect {
        systemBar.setNavigationBarAppearance(isLight = isLight)
    }
}
