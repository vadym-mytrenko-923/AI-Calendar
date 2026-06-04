package com.agents.app.demo.ui.navigation.model

import kotlinx.serialization.Serializable

sealed interface NavRoute

@Serializable
sealed interface AppNavRoute : NavRoute {
    @Serializable
    data object Main : AppNavRoute
}

@Serializable
sealed interface MainNavRoute : NavRoute {
    @Serializable
    data object Calendar : MainNavRoute
}
