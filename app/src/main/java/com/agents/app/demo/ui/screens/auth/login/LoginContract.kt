package com.agents.app.demo.ui.screens.auth.login

import android.os.Parcelable
import com.agents.app.demo.ui.core.error.model.UiError
import com.agents.app.demo.utils.StringResource
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginScreenState(
    val isLoading: Boolean = false,
    val errorMessage: StringResource? = null,
) : Parcelable

sealed interface LoginIntent {
    data object FieldTextChanged : LoginIntent
    data class LoginButtonClicked(val username: String, val password: String) : LoginIntent
}

sealed interface LoginEffect {
    data class ShowError(val error: UiError) : LoginEffect
}
