package com.katharina.recipesapp.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun getRecipes(): Flow<List<DbRecipe>>

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    fun getRecipe(recipeId: Int): Flow<DbRecipe>

    @Upsert
    fun upsertRecipe(recipe: DbRecipe)
}
