package com.agents.app.demo.ui.navigation.model

import kotlinx.serialization.Serializable

sealed interface NavRoute

@Serializable
sealed interface AppNavRoute : NavRoute {
    // Root
    @Serializable
    data object Login : AppNavRoute

    @Serializable
    data object Main : AppNavRoute
}

sealed interface MainNavRoute : NavRoute {
    // Tab graphs (top-level)
    @Serializable
    data object ProductsList : MainNavRoute

    @Serializable
    data object Settings : MainNavRoute

    // Details
    @Serializable
    data class ProductDetails(val id: Int = 0) : MainNavRoute
}
