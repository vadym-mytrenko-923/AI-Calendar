package com.agents.app.demo.ui.screens.main

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.agents.app.demo.ui.navigation.main.MainNavHost
import com.agents.app.demo.ui.navigation.main.MainNavigator
import com.agents.app.demo.ui.screens.main.composable.MainNavBarItem
import com.agents.app.demo.utils.isSelected
import com.agents.app.demo.utils.navigateTopLevel

@Composable
fun MainScreen(mainNavigator: MainNavigator) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = BottomBarDestination.entries.any { destination ->
        currentDestination.isSelected(destination.route)
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    BottomBarDestination.entries.forEach { destination ->
                        val isDestinationSelected = currentDestination.isSelected(destination.route)

                        MainNavBarItem(isSelected = isDestinationSelected, destination = destination) {
                            navController.navigateTopLevel(route = destination.route)
                        }
                    }
                }
            }
        }
    ) { padding ->
        MainNavHost(mainNavigator, navController, padding)
    }
}
