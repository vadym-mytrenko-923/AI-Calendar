package com.agents.app.demo.domain.base.logger

interface Logger {
    fun log(message: String)
    fun logClick(buttonName: String)
    fun logCrashAdditionalInfo(message: String)
    fun logException(throwable: Throwable)
}
