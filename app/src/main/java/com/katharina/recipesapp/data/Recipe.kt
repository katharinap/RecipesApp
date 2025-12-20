package com.katharina.recipesapp.data

import java.time.LocalDateTime

data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: List<String> = emptyList(),
    val directions: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val updatedAtRemotely: LocalDateTime? = null,
    var updatedAtLocally: LocalDateTime? = null,
) {
    fun needsUpdate(): Boolean {
        if (updatedAtRemotely == null) return true
        if (updatedAtLocally == null) return true
        return updatedAtRemotely.isAfter(updatedAtLocally)
    }
}
