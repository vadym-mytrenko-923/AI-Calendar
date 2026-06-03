package com.agents.app.demo.ui.screens.auth.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.agents.app.demo.ui.core.alert.LocalAppAlert
import com.agents.app.demo.ui.screens.auth.login.composable.LoginContent
import com.agents.app.demo.ui.theme.AiAgenticAppTheme
import com.agents.app.demo.ui.theme.appColorsScheme
import com.agents.app.demo.ui.theme.marginPrimary2X
import com.agents.app.demo.utils.NonTranslatableStringResource
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    val appAlert = LocalAppAlert.current
    val context = LocalContext.current
    val uiState by viewModel.uiStateFlow.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.uiEffectFlow.collectLatest { effect ->
            when (effect) {
                is LoginEffect.ShowError -> appAlert.showAlert(
                    title = effect.error.title?.getString(context),
                    message = effect.error.message?.getString(context),
                    icon = effect.error.icon
                )
            }
        }
    }

    LoginScreenContent(
        uiState = uiState,
        onUserIntent = viewModel::onUserIntent
    )
}

@Composable
private fun LoginScreenContent(
    uiState: LoginScreenState,
    onUserIntent: (LoginIntent) -> Unit = {},
) {
    LoginScreenScaffold(
        uiState = uiState,
        content = {
            LoginContent(
                errorMessage = uiState.errorMessage,
                isLoading = uiState.isLoading,
                onFieldTextChanged = { onUserIntent(LoginIntent.FieldTextChanged) },
                onLoginButtonClicked = { username, password ->
                    onUserIntent(LoginIntent.LoginButtonClicked(username, password))
                }
            )
        }
    )
}

@Composable
private fun LoginScreenScaffold(
    uiState: LoginScreenState,
    content: @Composable () -> Unit = {},
) {
    Scaffold(containerColor = MaterialTheme.appColorsScheme.background) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = marginPrimary2X)
        ) {
            content()
        }
    }
}

@Composable
@Preview
private fun LoginScreenPreview() {
    AiAgenticAppTheme {
        LoginScreenContent(uiState = LoginScreenState(isLoading = false, errorMessage = null))
    }
}

@Composable
@Preview
private fun LoginScreenErrorPreview() {
    AiAgenticAppTheme {
        LoginScreenContent(
            uiState = LoginScreenState(
                isLoading = false,
                errorMessage = NonTranslatableStringResource("Username is not valid")
            )
        )
    }
}
