package com.katharina.recipesapp.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("email_address")
    var userName: String,
    @SerialName("password")
    var password: String,
)
