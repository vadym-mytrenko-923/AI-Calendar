package com.agents.app.demo.ui.navigation.base

import com.agents.app.demo.ui.navigation.model.NavRoute
import com.agents.app.demo.ui.navigation.model.NavigationEffect
import kotlinx.coroutines.flow.Flow

interface Navigator<R : NavRoute> {
    val navigationEffect: Flow<NavigationEffect<R>>
    suspend fun navigateTo(route: R)
    suspend fun back()
    suspend fun <T> backWithResult(result: T)
    suspend fun returnTo(route: R)
    suspend fun <T> returnToWithResult(route: R, result: T)
    suspend fun returnToDestination(destinationId: Int)
    suspend fun <T> returnToDestinationWithResult(destinationId: Int, result: T)
}
