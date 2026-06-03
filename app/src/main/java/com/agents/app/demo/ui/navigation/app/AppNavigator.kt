package com.agents.app.demo.ui.navigation.app

import com.agents.app.demo.ui.navigation.base.BaseNavigator
import com.agents.app.demo.ui.navigation.model.AppNavRoute
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNavigator @Inject constructor() : BaseNavigator<AppNavRoute>()
