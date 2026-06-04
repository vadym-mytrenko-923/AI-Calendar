package com.ai.calendar.demo.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

private const val DECODE_BODY_HEADER = "Accept-Encoding"
private const val DECODE_BODY_HEADER_VALUE = "identity"

class GzipDecodingInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
        request.addHeader(DECODE_BODY_HEADER, DECODE_BODY_HEADER_VALUE)
        request.method(original.method, original.body)
        return chain.proceed(request.build())
    }
}
