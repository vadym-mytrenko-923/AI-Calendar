package com.agents.app.demo.ui.screens.main

import androidx.annotation.StringRes
import com.agents.app.demo.R
import com.agents.app.demo.ui.navigation.model.MainNavRoute
import com.agents.app.demo.ui.theme.AppIcons

enum class BottomBarDestination(val route: MainNavRoute, @param:StringRes val label: Int, val icon: Int) {
    Products(route = MainNavRoute.ProductsList, label = R.string.mainBniProducts, icon = AppIcons.Products),
    Settings(route = MainNavRoute.Settings, label = R.string.mainBniSettings, icon = AppIcons.Settings)
}
