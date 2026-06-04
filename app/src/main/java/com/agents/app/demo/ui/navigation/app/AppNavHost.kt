package com.agents.app.demo.ui.navigation.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.agents.app.demo.ui.navigation.base.NavigationEffectHandler
import com.agents.app.demo.ui.navigation.main.MainNavigator
import com.agents.app.demo.ui.navigation.model.AppNavRoute
import com.agents.app.demo.ui.screens.main.MainScreen

@Composable
fun AppNavHost(
    appNavigator: AppNavigator,
    mainNavigator: MainNavigator,
) {
    val navController = rememberNavController()

    NavigationEffectHandler(navigator = appNavigator, navController = navController)

    NavHost(
        navController = navController,
        startDestination = AppNavRoute.Main,
    ) {
        composable<AppNavRoute.Main> {
            MainScreen(mainNavigator = mainNavigator)
        }
    }
}
