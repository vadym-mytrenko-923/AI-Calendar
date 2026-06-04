package com.agents.app.demo.ui.screens.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.agents.app.demo.ui.navigation.main.MainNavHost
import com.agents.app.demo.ui.navigation.main.MainNavigator

@Composable
fun MainScreen(mainNavigator: MainNavigator) {
    val navController = rememberNavController()
    MainNavHost(mainNavigator, navController, PaddingValues())
}
