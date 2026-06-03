package com.agents.app.demo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.agents.app.demo.ui.core.alert.AppAlertProvider
import com.agents.app.demo.ui.core.composable.other.SystemBarProvider
import com.agents.app.demo.ui.navigation.app.AppNavHost
import com.agents.app.demo.ui.navigation.app.AppNavigator
import com.agents.app.demo.ui.navigation.main.MainNavigator
import com.agents.app.demo.ui.screens.envSplash.EnvironmentSplashScreen
import com.agents.app.demo.ui.theme.AiAgenticAppTheme
import com.agents.app.demo.utils.AppInfo
import com.agents.app.demo.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var appNavigator: AppNavigator

    @Inject
    lateinit var mainNavigator: MainNavigator

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                return@setKeepOnScreenCondition viewModel.isUserLoggedInFlow.value == null
            }
        }
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            AiAgenticAppTheme {
                var showEnvironmentSplash by rememberSaveable { mutableStateOf(AppInfo.isTestEnv) }

                LaunchedEffect(showEnvironmentSplash) {
                    if (showEnvironmentSplash) {
                        delay(Constants.ENVIRONMENT_SLASH_DURATION)
                        showEnvironmentSplash = false
                    }
                }

                SystemBarProvider {
                    AppAlertProvider {
                        if (showEnvironmentSplash) {
                            EnvironmentSplashScreen()
                        } else {
                            AppNavHost(
                                appNavigator = appNavigator,
                                mainNavigator = mainNavigator,
                                isUserLoggedInFlow = viewModel.isUserLoggedInFlow
                            )
                        }
                    }
                }
            }
        }
    }
}
