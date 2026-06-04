package com.agents.app.demo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.agents.app.demo.ui.core.alert.AppAlertProvider
import com.agents.app.demo.ui.core.composable.other.SystemBarProvider
import com.agents.app.demo.ui.navigation.app.AppNavHost
import com.agents.app.demo.ui.navigation.app.AppNavigator
import com.agents.app.demo.ui.navigation.main.MainNavigator
import com.agents.app.demo.ui.theme.AiAgenticAppTheme
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
            AiAgenticAppTheme {
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
