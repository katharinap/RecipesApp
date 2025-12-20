package com.katharina.recipesapp.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeDetailsResponse(
    val id: Int,
    @SerialName(value = "name")
    val title: String,
    val directions: List<String>,
    val ingredients: List<String>,
    val tags: List<String>,
    @SerialName(value = "picture_path")
    val picturePath: String,
    @SerialName(value = "updated_at")
    val updatedAt: String,
)
