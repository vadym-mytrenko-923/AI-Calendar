package com.agents.app.demo.ui.screens.auth.login.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.agents.app.demo.R
import com.agents.app.demo.ui.core.composable.button.BtnPrimary
import com.agents.app.demo.ui.theme.marginPrimary2X
import com.agents.app.demo.utils.StringResource

@Composable
fun LoginContent(
    errorMessage: StringResource?,
    isLoading: Boolean,
    onFieldTextChanged: () -> Unit,
    onLoginButtonClicked: (String, String) -> Unit,
) {
    val scrollState = rememberScrollState()
    val usernameFieldState = rememberTextFieldState()
    val passwordFieldState = rememberTextFieldState()

    LaunchedEffect(usernameFieldState.text, passwordFieldState.text) {
        onFieldTextChanged()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.weight(0.15f))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            state = usernameFieldState,
            label = { Text(stringResource(R.string.loginTfUserName)) },
            isError = errorMessage != null,
            supportingText = {
                errorMessage?.let { Text(text = it.getString()) }
            },
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            enabled = !isLoading,
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = marginPrimary2X),
            state = passwordFieldState,
            label = { Text(stringResource(R.string.loginTfPassword)) },
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            onKeyboardAction = {
                onLoginButtonClicked(
                    usernameFieldState.text.toString(),
                    passwordFieldState.text.toString()
                )
            },
            enabled = !isLoading,
        )
        Spacer(modifier = Modifier.weight(1f))
        BtnPrimary(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = marginPrimary2X),
            text = stringResource(id = R.string.loginBtnLogin),
            isEnabled = !isLoading && usernameFieldState.text.isNotBlank() && passwordFieldState.text.isNotBlank(),
            onClick = { onLoginButtonClicked(usernameFieldState.text.toString(), passwordFieldState.text.toString()) }
        )
    }
}
