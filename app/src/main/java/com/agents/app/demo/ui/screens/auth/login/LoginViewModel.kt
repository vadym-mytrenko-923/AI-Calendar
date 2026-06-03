package com.agents.app.demo.ui.screens.auth.login

import androidx.lifecycle.SavedStateHandle
import com.agents.app.demo.domain.features.auth.model.LoginParams
import com.agents.app.demo.domain.features.auth.usecase.LoginUseCase
import com.agents.app.demo.ui.base.BaseViewModel
import com.agents.app.demo.ui.navigation.app.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val appNavigator: AppNavigator,
    private val loginUseCase: LoginUseCase,
) : BaseViewModel<LoginScreenState, LoginIntent, LoginEffect>(
    initialState = LoginScreenState(),
    savedStateHandle = savedStateHandle
) {
    override fun reduceIntent(intent: LoginIntent) {
        launchViewModelScope {
            when (intent) {
                is LoginIntent.LoginButtonClicked -> {
                    updateUiState { it.copy(isLoading = true) }
                    loginUseCase.invoke(LoginParams(username = intent.username, password = intent.password)).onSuccess {
                        // TODO: navigate to Home
                    }.onFailure { error ->
                        val uiError = parseError(error)
                        sendUiEffect(effect = LoginEffect.ShowError(uiError))
                        updateUiState { it.copy(errorMessage = uiError.message) }
                    }
                    updateUiState { it.copy(isLoading = false) }
                }

                LoginIntent.FieldTextChanged -> {
                    updateUiState { it.copy(errorMessage = null) }
                }
            }
        }
    }
}
