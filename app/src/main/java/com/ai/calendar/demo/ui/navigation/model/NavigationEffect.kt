package com.ai.calendar.demo.ui.navigation.model

sealed interface NavigationEffect<out R : NavRoute> {
    data class Navigate<R : NavRoute>(val route: R) : NavigationEffect<R>
    data class ReturnTo<R : NavRoute>(val route: R) : NavigationEffect<R>
    data object Back : NavigationEffect<Nothing>
    data class BackWithResult<T>(val result: T) : NavigationEffect<Nothing>
    data class ReturnToRouteWithResult<R : NavRoute, T>(val route: R, val result: T) : NavigationEffect<Nothing>
    data class ReturnToDestination(val destinationId: Int) : NavigationEffect<Nothing>
    data class ReturnToDestinationWithResult<T>(val destinationId: Int, val result: T) : NavigationEffect<Nothing>
}
