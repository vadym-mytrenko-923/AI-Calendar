package com.agents.app.demo.data.remote.interceptor

import com.agents.app.demo.data.local.storage.user.UserStorage
import com.agents.app.demo.utils.notNullOrEmpty
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val userStorage: UserStorage,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val request = chain.request()
        val requestToProceed =
            userStorage.getAccessToken().notNullOrEmpty { token ->
                request.newBuilder()
                    .header(AUTH_HEADER, "$HEADER_TYPE $token")
                    .build()
            } ?: request
        return@runBlocking chain.proceed(requestToProceed)
    }

    companion object {
        const val AUTH_HEADER = "Authorization"
        const val HEADER_TYPE = "Bearer"
    }
}
