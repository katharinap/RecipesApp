package com.katharina.recipesapp.data

import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun getAllRecipesStream(): Flow<List<Recipe>>

    fun getRecipeById(id: Int): Flow<Recipe?>
}
