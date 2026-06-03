package com.agents.app.demo.ui.navigation.base

import com.agents.app.demo.ui.navigation.model.NavRoute
import com.agents.app.demo.ui.navigation.model.NavigationEffect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseNavigator<R : NavRoute> : Navigator<R> {
    private val effectChannel = Channel<NavigationEffect<R>>(Channel.BUFFERED)
    override val navigationEffect: Flow<NavigationEffect<R>> = effectChannel.receiveAsFlow()

    override suspend fun navigateTo(route: R) {
        effectChannel.send(NavigationEffect.Navigate(route))
    }

    override suspend fun back() {
        effectChannel.send(NavigationEffect.Back)
    }

    override suspend fun <T> backWithResult(result: T) {
        effectChannel.send(NavigationEffect.BackWithResult(result))
    }

    override suspend fun returnTo(route: R) {
        effectChannel.send(NavigationEffect.ReturnTo(route))
    }

    override suspend fun <T> returnToWithResult(route: R, result: T) {
        effectChannel.send(NavigationEffect.ReturnToRouteWithResult(route, result))
    }

    override suspend fun returnToDestination(destinationId: Int) {
        effectChannel.send(NavigationEffect.ReturnToDestination(destinationId))
    }

    override suspend fun <T> returnToDestinationWithResult(destinationId: Int, result: T) {
        effectChannel.send(NavigationEffect.ReturnToDestinationWithResult(destinationId, result))
    }
}
