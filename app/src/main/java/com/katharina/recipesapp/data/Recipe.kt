package com.katharina.recipesapp.data

import com.katharina.recipesapp.data.network.Constants
import java.time.LocalDateTime

data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: List<String> = emptyList(),
    val directions: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val imageUrl: String? = null,
    val updatedAtRemotely: LocalDateTime? = null,
    val updatedAtLocally: LocalDateTime? = null,
    val language: String? = null,
    val starred: Boolean = false,
) {
    fun getRemoteImageUrl(): String? {
        if (imageUrl == null) {
            return null
        } else {
            return Constants.IMAGE_BASE_URL + imageUrl
        }
    }

    fun isGerman(): Boolean = language == "german"
}
