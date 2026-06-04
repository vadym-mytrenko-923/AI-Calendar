package com.ai.calendar.demo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ai.calendar.demo.ui.core.alert.AppAlertProvider
import com.ai.calendar.demo.ui.core.composable.other.SystemBarProvider
import com.ai.calendar.demo.ui.navigation.app.AppNavHost
import com.ai.calendar.demo.ui.navigation.app.AppNavigator
import com.ai.calendar.demo.ui.navigation.main.MainNavigator
import com.ai.calendar.demo.ui.theme.AiCalendarTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var appNavigator: AppNavigator

    @Inject
    lateinit var mainNavigator: MainNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            AiCalendarTheme {
                SystemBarProvider {
                    AppAlertProvider {
                        AppNavHost(
                            appNavigator = appNavigator,
                            mainNavigator = mainNavigator,
                        )
                    }
                }
            }
        }
    }
}
