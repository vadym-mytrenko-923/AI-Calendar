package com.ai.calendar.demo.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TIMEOUT = 60

class ConnectionTimeoutInterceptor @Inject constructor() :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return chain
            .withConnectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .withReadTimeout(TIMEOUT, TimeUnit.SECONDS)
            .withWriteTimeout(TIMEOUT, TimeUnit.SECONDS)
            .proceed(request)
    }
}
