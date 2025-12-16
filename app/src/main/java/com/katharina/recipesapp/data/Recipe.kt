package com.katharina.recipesapp.data

import java.time.LocalDateTime

data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: List<String> = emptyList(),
    val directions: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val updatedAtRemotely: LocalDateTime? = null,
    val updatedAtLocally: LocalDateTime? = null,
)
