package com.agents.app.demo.ui.navigation.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.agents.app.demo.ui.navigation.base.NavigationEffectHandler
import com.agents.app.demo.ui.navigation.main.MainNavigator
import com.agents.app.demo.ui.navigation.model.AppNavRoute
import com.agents.app.demo.ui.screens.auth.login.LoginScreen
import com.agents.app.demo.ui.screens.main.MainScreen
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AppNavHost(
    appNavigator: AppNavigator,
    mainNavigator: MainNavigator,
    isUserLoggedInFlow: StateFlow<Boolean?>
) {
    val isUserLoggedInRawValue by isUserLoggedInFlow.collectAsStateWithLifecycle()
    val isUserLoggedIn = isUserLoggedInRawValue == true

    key(isUserLoggedIn) {
        val navController = rememberNavController()

        NavigationEffectHandler(navigator = appNavigator, navController = navController)

        NavHost(
            navController = navController,
            startDestination = if (isUserLoggedIn) AppNavRoute.Main else AppNavRoute.Login
        ) {
            composable<AppNavRoute.Login> {
                LoginScreen()
            }
            composable<AppNavRoute.Main> {
                MainScreen(mainNavigator = mainNavigator)
            }
        }
    }
}
