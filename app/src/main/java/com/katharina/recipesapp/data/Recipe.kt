package com.katharina.recipesapp.data

data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: List<String> = emptyList(),
    val directions: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val updatedAtRemotely: String = "",
    val updatedAtLocally: String = "",
)
