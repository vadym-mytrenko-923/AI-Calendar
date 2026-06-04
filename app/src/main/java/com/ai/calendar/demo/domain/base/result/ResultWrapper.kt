package com.ai.calendar.demo.domain.base.result

suspend fun <T> useResultWrapper(
    action: suspend () -> T
): Result<T> = try {
    val actionResult = action()
    Result.success(actionResult)
} catch (t: Throwable) {
    Result.failure(t)
}
