package com.agents.app.demo.data.features.auth.mapper

import com.agents.app.demo.data.features.auth.local.UserData
import com.agents.app.demo.data.features.auth.remote.model.AuthResponse
import com.agents.app.demo.data.features.auth.remote.model.LoginRequestDto
import com.agents.app.demo.domain.features.auth.model.LoginParams

fun LoginParams.toRequestDto(): LoginRequestDto = LoginRequestDto(
    username = username,
    password = password
)

fun AuthResponse.toDataModel(): UserData = UserData(
    username = username,
    email = email,
    firstName = firstName,
    lastName = lastName
)
