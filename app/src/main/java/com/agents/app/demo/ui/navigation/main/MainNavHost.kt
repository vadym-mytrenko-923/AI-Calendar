package com.agents.app.demo.ui.navigation.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.agents.app.demo.ui.navigation.base.NavigationEffectHandler
import com.agents.app.demo.ui.navigation.model.MainNavRoute
import com.agents.app.demo.ui.screens.product.ProductsScreen
import com.agents.app.demo.ui.screens.product.details.ProductDetailsScreen
import com.agents.app.demo.ui.screens.settings.SettingsScreen

@Composable
fun MainNavHost(mainNavigator: MainNavigator, navController: NavHostController, padding: PaddingValues) {
    NavigationEffectHandler(navigator = mainNavigator, navController = navController)

    NavHost(
        navController = navController,
        startDestination = MainNavRoute.ProductsList,
        modifier = Modifier.padding(padding)
    ) {
        composable<MainNavRoute.ProductsList> {
            ProductsScreen()
        }
        composable<MainNavRoute.Settings> {
            SettingsScreen()
        }
        composable<MainNavRoute.ProductDetails> {
            ProductDetailsScreen()
        }
    }
}
