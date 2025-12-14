package com.katharina.recipesapp.data.network

import com.katharina.recipesapp.data.credentials.CredentialsDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class ApiAuthenticator
    @Inject
    constructor(
        private val loginService: LoginService,
        private val refreshTokenService: RefreshTokenService,
        private val credentialsDataStore: CredentialsDataStore,
    ) : Authenticator {
        override fun authenticate(
            route: Route?,
            response: Response,
        ): Request? {
            val currentAccessToken = runBlocking { credentialsDataStore.accessTokenFlow().first() }
            val refreshToken = runBlocking { credentialsDataStore.refreshTokenFlow().first() }
            val userName = runBlocking { credentialsDataStore.userNameFlow().first() }
            val password = runBlocking { credentialsDataStore.passwordFlow().first() }

            synchronized(this) {
                val updatedAccessToken = runBlocking { credentialsDataStore.accessTokenFlow().first() }

                val token =
                    if (currentAccessToken != updatedAccessToken) {
                        updatedAccessToken
                    } else {
                        val newSessionResponse = runBlocking { refreshTokenService.refreshToken(refreshToken) }
                        if (newSessionResponse.isSuccessful) {
                            runBlocking {
                                credentialsDataStore.updateAccessToken(newSessionResponse.body()?.accessToken ?: "")
                                credentialsDataStore.updateRefreshToken(newSessionResponse.body()?.refreshToken ?: "")
                            }
                            newSessionResponse.body()?.accessToken
                        } else {
                            val loginResponse = runBlocking { loginService.login(LoginRequest(userName = userName, password = password)) }
                            if (loginResponse.isSuccessful) {
                                runBlocking {
                                    credentialsDataStore.updateAccessToken(loginResponse.body()?.accessToken ?: "")
                                    credentialsDataStore.updateRefreshToken(loginResponse.body()?.refreshToken ?: "")
                                }
                                loginResponse.body()?.accessToken
                            } else {
                                null
                            }
                        }
                    }

                return if (token != null) {
                    response.request
                        .newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                } else {
                    null
                }
            }
        }
    }
