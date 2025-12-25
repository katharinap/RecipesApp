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

    suspend fun searchRecipes(query: String): Flow<List<Recipe>>

    suspend fun getRecipesWithTag(tag: String): Flow<List<Recipe>>

    suspend fun updateRecipes(force: Boolean): String

    suspend fun updateRecipe(recipeId: Int): String
}

@Singleton
class DefaultRecipesRepository
    @Inject
    constructor(
        private val dbRepository: DbRepository,
        private val networkRepository: NetworkRepository,
    ) : RecipeRepository {
        override suspend fun getAllRecipes(): Flow<List<Recipe>> = dbRepository.getRecipesFlow()

        override suspend fun searchRecipes(query: String): Flow<List<Recipe>> = dbRepository.searchRecipes(query)

        override suspend fun getRecipesWithTag(tag: String): Flow<List<Recipe>> = dbRepository.getRecipesWithTag(tag)

        override suspend fun getRecipeById(recipeId: Int): Flow<Recipe?> = dbRepository.getRecipeFlow(recipeId)

        override suspend fun updateRecipes(force: Boolean): String {
            val response = networkRepository.getRecipes()

            if (response is NetworkResult.Success) {
                var updateCount = 0

                val localRecipes = dbRepository.getAllRecipes().map { it.id to it.updatedAtLocally }.toMap()

                response.data.forEach { recipeRemote ->
                    val needsUpdate = force || localRecipes.get(recipeRemote.id)?.isBefore(recipeRemote.updatedAtRemotely) ?: true
                    if (needsUpdate) {
                        // dbRepository.updateRecipe(recipeRemote)
                        updateRecipe(recipeRemote.id)
                        updateCount++
                    }
                }
                return "Successfully fetched $updateCount recipes"
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
