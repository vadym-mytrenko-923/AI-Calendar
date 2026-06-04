package com.ai.calendar.demo.ui.screens.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.ai.calendar.demo.ui.navigation.main.MainNavHost
import com.ai.calendar.demo.ui.navigation.main.MainNavigator

@Composable
fun MainScreen(mainNavigator: MainNavigator) {
    val navController = rememberNavController()
    MainNavHost(mainNavigator, navController, PaddingValues())
}
