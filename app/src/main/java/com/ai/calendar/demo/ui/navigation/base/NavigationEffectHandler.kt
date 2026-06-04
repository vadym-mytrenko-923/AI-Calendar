package com.ai.calendar.demo.ui.navigation.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.ai.calendar.demo.ui.navigation.model.NavRoute
import com.ai.calendar.demo.ui.navigation.model.NavigationEffect
import kotlinx.coroutines.flow.collectLatest

const val NAV_RESULT_KEY = "result"

@Composable
fun <R : NavRoute> NavigationEffectHandler(navigator: Navigator<R>, navController: NavController) {
    LaunchedEffect(navigator) {
        navigator.navigationEffect.collectLatest { effect ->
            when (effect) {
                is NavigationEffect.Navigate -> navController.navigate(effect.route)

                NavigationEffect.Back -> navController.popBackStackIfPossible()

                is NavigationEffect.BackWithResult<*> -> {
                    navController.previousBackStackEntry?.savedStateHandle?.set(NAV_RESULT_KEY, effect.result)
                    navController.popBackStackIfPossible()
                }

                is NavigationEffect.ReturnTo -> navController.popBackStackIfPossible(effect.route)

                is NavigationEffect.ReturnToRouteWithResult<*, *> -> {
                    navController.getBackStackEntry(effect.route).savedStateHandle[NAV_RESULT_KEY] = effect.result
                    navController.popBackStackIfPossible(effect.route)
                }

                is NavigationEffect.ReturnToDestination -> {
                    navController.popBackStack(effect.destinationId, inclusive = false)
                }

                is NavigationEffect.ReturnToDestinationWithResult<*> -> {
                    navController.getBackStackEntry(effect.destinationId).savedStateHandle[NAV_RESULT_KEY] = effect.result
                    navController.popBackStack(effect.destinationId, inclusive = false)
                }
            }
        }
    }
}

private fun NavController.popBackStackIfPossible(route: NavRoute? = null) {
    val currentRoute = currentBackStackEntry?.destination?.route
    val previousRoute = previousBackStackEntry?.destination?.route
    if (currentRoute != null && previousRoute != null) {
        route?.let { popBackStack(route = route, inclusive = false) } ?: popBackStack()
    }
}
