package com.ai.calendar.demo.ui.screens.calendar.utils

import android.util.Patterns

fun isValidEmail(email: String): Boolean = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
