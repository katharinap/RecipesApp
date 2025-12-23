package com.katharina.recipesapp.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun getRecipesFlow(): Flow<List<DbRecipe>>

    @Query(value = "SELECT * FROM recipes WHERE title LIKE '%' || :query || '%'")
    fun searchRecipes(query: String): Flow<List<DbRecipe>>

    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<DbRecipe>

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    fun getRecipeFlow(recipeId: Int): Flow<DbRecipe?>

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    suspend fun getRecipe(recipeId: Int): DbRecipe?

    @Upsert
    fun upsertRecipe(recipe: DbRecipe)
}
