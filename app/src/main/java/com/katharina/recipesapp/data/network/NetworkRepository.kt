package com.katharina.recipesapp.data.network

import com.katharina.recipesapp.data.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

interface NetworkRepository {
    suspend fun getRecipes(): NetworkResult<List<Recipe>>

    suspend fun getRecipe(recipeId: Int): NetworkResult<Recipe>
}

@Singleton
class DefaultNetworkRepository
    @Inject
    constructor(
        private val loginService: LoginService,
//        private val refreshTokenService: RefreshTokenService,
        private val apiService: ApiService,
    ) : NetworkRepository {
        override suspend fun getRecipes(): NetworkResult<List<Recipe>> =
            withContext(Dispatchers.IO) {
                try {
                    val response = apiService.getRecipes()
                    if (response.isSuccessful) {
                        val recipes: List<Recipe> = response.body()?.map { it.toRecipe() } ?: emptyList()
                        NetworkResult.Success(recipes)
                    } else {
                        NetworkResult.Error(Exception("Error: ${response.code()}"))
                    }
                } catch (e: Exception) {
                    NetworkResult.Error(e)
                }
            }

        private fun RecipeListResponseItem.toRecipe(): Recipe =
            Recipe(
                id = id,
                title = name,
                updatedAtRemotely =
                    LocalDateTime.ofInstant(
                        java.time.Instant.parse(updatedAt),
                        java.time.ZoneId.systemDefault(),
                    ),
            )

        override suspend fun getRecipe(recipeId: Int): NetworkResult<Recipe> =
            withContext(Dispatchers.IO) {
                try {
                    val response = apiService.getRecipe(recipeId)
                    if (response.isSuccessful) {
                        val recipe: Recipe =
                            response.body()?.toRecipe() ?: throw Exception("Recipe not found")
                        NetworkResult.Success(recipe)
                    } else {
                        NetworkResult.Error(Exception("Error: ${response.code()}"))
                    }
                } catch (e: Exception) {
                    NetworkResult.Error(e)
                }
            }

        private fun RecipeDetailsResponse.toRecipe(): Recipe =
            Recipe(
                id = id,
                title = title,
                ingredients = ingredients,
                directions = directions,
                tags = tags,
            )
    }
