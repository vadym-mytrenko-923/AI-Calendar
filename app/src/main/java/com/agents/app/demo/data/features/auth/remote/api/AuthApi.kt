package com.agents.app.demo.data.features.auth.remote.api

import com.agents.app.demo.data.features.auth.remote.model.AuthResponse
import com.agents.app.demo.data.features.auth.remote.model.LoginRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequestDto): AuthResponse
}
