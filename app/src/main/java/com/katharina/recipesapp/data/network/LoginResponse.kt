package com.katharina.recipesapp.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("refresh_token")
    var refreshToken: String,
    @SerialName("access_token")
    var accessToken: String,
)
