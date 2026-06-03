package com.agents.app.demo.data.common.logger

import com.agents.app.demo.domain.base.logger.Logger
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLogger @Inject constructor() : Logger {
    // TODO: Uncomment Crashlytics calls below if Firebase Crashlytics is added to the project or replace with another analytics provider
    // private val crashlytics get() = FirebaseCrashlytics.getInstance()

    override fun log(message: String) {
        // crashlytics.log(message)
        logInternal(message)
    }

    override fun logClick(buttonName: String) {
        // crashlytics.log("Clicked \"$buttonName\"")
        logInternal("Clicked \"$buttonName\"")
    }

    override fun logCrashAdditionalInfo(message: String) {
        // crashlytics.log(message)
        logInternal(message)
    }

    override fun logException(throwable: Throwable) {
        // crashlytics.recordException(throwable)
        logInternal(throwable.message ?: throwable.toString())
    }

    private fun logInternal(message: String) {
        Timber.e(message)
    }
}
