package com.katharina.recipesapp.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeListResponseItem(
    val id: Int,
    val name: String,
    @SerialName(value = "updated_at")
    val updatedAt: String,
)
