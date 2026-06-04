package com.ai.calendar.demo.ui.navigation.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ai.calendar.demo.ui.navigation.base.NavigationEffectHandler
import com.ai.calendar.demo.ui.navigation.model.MainNavRoute
import com.ai.calendar.demo.ui.screens.calendar.CalendarScreen

@Composable
fun MainNavHost(
    mainNavigator: MainNavigator,
    navController: NavHostController,
    padding: PaddingValues = PaddingValues()
) {
    NavigationEffectHandler(navigator = mainNavigator, navController = navController)

    NavHost(
        navController = navController,
        startDestination = MainNavRoute.Calendar,
        modifier = Modifier.padding(padding),
    ) {
        composable<MainNavRoute.Calendar> {
            CalendarScreen()
        }
    }
}
