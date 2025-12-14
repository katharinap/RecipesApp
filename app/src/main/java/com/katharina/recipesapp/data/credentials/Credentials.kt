package com.katharina.recipesapp.data.credentials

import kotlinx.serialization.Serializable

@Serializable
data class Credentials(
    val userName: String = "",
    val password: String = "",
    val refreshToken: String = "",
    val accessToken: String = "",
    val isEncrypted: Boolean = false,
)
