package com.katharina.recipesapp.data

import com.katharina.recipesapp.data.db.DbRepository
import com.katharina.recipesapp.data.network.NetworkRepository
import com.katharina.recipesapp.data.network.NetworkResult
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

interface RecipeRepository {
    suspend fun getAllRecipes(): Flow<List<Recipe>>

    suspend fun getRecipeById(recipeId: Int): Flow<Recipe?>

    suspend fun updateRecipes(): String

    suspend fun updateRecipe(recipeId: Int): String
}

@Singleton
class DefaultRecipesRepository
    @Inject
    constructor(
        private val dbRepository: DbRepository,
        private val networkRepository: NetworkRepository,
    ) : RecipeRepository {
        override suspend fun getAllRecipes(): Flow<List<Recipe>> = dbRepository.getRecipes()

        override suspend fun getRecipeById(recipeId: Int): Flow<Recipe?> = dbRepository.getRecipe(recipeId)

        override suspend fun updateRecipes(): String {
            val response = networkRepository.getRecipes()
            if (response is NetworkResult.Success) {
                response.data.forEach { recipe ->
                    dbRepository.updateRecipe(recipe)
                }
                return "Successfully fetched ${response.data.size} recipes"
            } else {
                return (response as NetworkResult.Error).exception.message ?: "Unknown error"
            }
        }

        override suspend fun updateRecipe(recipeId: Int): String {
            val response = networkRepository.getRecipe(recipeId)
            if (response is NetworkResult.Success) {
                val recipe = response.data.copy(updatedAtLocally = LocalDateTime.now())
                dbRepository.updateRecipe(recipe)
                return "Successfully fetched recipe ${response.data.title}"
            } else {
                return (response as NetworkResult.Error).exception.message ?: "Unknown error"
            }
        }
    }
