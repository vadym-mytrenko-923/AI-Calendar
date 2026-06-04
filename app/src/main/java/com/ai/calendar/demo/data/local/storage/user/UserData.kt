package com.ai.calendar.demo.data.local.storage.user

data class UserData(
    val id: Int = 0,
    val username: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val image: String = "",
    val accessToken: String = "",
    val refreshToken: String = "",
)
