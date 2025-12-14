package com.katharina.recipesapp.data.network

import com.katharina.recipesapp.data.credentials.CredentialsDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class RefreshTokenInterceptor
    @Inject
    constructor(
        private val credentialsDataStore: CredentialsDataStore,
    ) : Interceptor {
        companion object {
            const val AUTHORIZATION_HEADER = "Authorization"
            const val BEARER_PREFIX = "Bearer "
        }

        override fun intercept(chain: Interceptor.Chain): Response {
            val token = runBlocking { credentialsDataStore.refreshTokenFlow().first() }
            val request =
                chain
                    .request()
                    .newBuilder()
                    .addHeader(AUTHORIZATION_HEADER, "$BEARER_PREFIX$token")

            return chain.proceed(request.build())
        }
    }
