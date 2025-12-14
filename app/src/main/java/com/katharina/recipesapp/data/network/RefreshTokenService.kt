package com.katharina.recipesapp.data.network

import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface RefreshTokenService {
    @POST(Constants.REFRESH_TOKEN_URL)
    suspend fun refreshToken(
        @Header("Authorization") refreshToken: String,
    ): Response<RefreshTokenResponse>
}
