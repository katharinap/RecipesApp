package com.katharina.recipesapp.data.network

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST(Constants.LOGIN_URL)
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): LoginResponse
}
