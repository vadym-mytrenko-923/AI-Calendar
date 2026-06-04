package com.ai.calendar.demo.utils

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.ai.calendar.demo.ui.navigation.model.NavRoute

fun NavDestination?.isSelected(route: NavRoute): Boolean = this?.hierarchy?.any { it.hasRoute(route::class) } == true

fun NavHostController.navigateTopLevel(route: NavRoute) = navigate(route) {
    popUpTo(graph.findStartDestination().id) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}
