package com.katharina.recipesapp

import kotlinx.serialization.Serializable

sealed class AppDestination {
    @Serializable
    data object RecipeList : AppDestination()

    @Serializable
    data class RecipeDetails(
        val recipeId: Int? = -1,
    ) : AppDestination()
}
