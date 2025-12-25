package com.katharina.recipesapp.data.db

import com.katharina.recipesapp.data.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface DbRepository {
    suspend fun getRecipesFlow(): Flow<List<Recipe>>

    suspend fun searchRecipes(query: String): Flow<List<Recipe>>

    suspend fun getRecipesWithTag(tag: String): Flow<List<Recipe>>

    suspend fun getAllRecipes(): List<Recipe>

    suspend fun getRecipeFlow(recipeId: Int): Flow<Recipe?>

    suspend fun updateRecipe(recipe: Recipe)
}

@Singleton
class DefaultDbRepository
    @Inject
    constructor(
        private val recipeDao: RecipeDao,
    ) : DbRepository {
        override suspend fun getRecipesFlow(): Flow<List<Recipe>> = recipeDao.getRecipesFlow().map { list -> list.map { it.toRecipe() } }

        override suspend fun searchRecipes(query: String): Flow<List<Recipe>> =
            recipeDao.searchRecipes(query).map { list -> list.map { it.toRecipe() } }

        override suspend fun getRecipesWithTag(tag: String): Flow<List<Recipe>> =
            recipeDao.getRecipesWithTag(tag).map { list ->
                list.map {
                    it.toRecipe()
                }
            }

        override suspend fun getAllRecipes(): List<Recipe> = recipeDao.getAllRecipes().map { it.toRecipe() }

        override suspend fun getRecipeFlow(recipeId: Int): Flow<Recipe?> = recipeDao.getRecipeFlow(recipeId).map { it?.toRecipe() }

        override suspend fun updateRecipe(recipe: Recipe) {
            withContext(Dispatchers.IO) {
                recipeDao.upsertRecipe(recipe.toDbRecipe())
            }
        }

        private fun DbRecipe.toRecipe(): Recipe =
            Recipe(
                id = id,
                title = title,
                ingredients = ingredients,
                directions = directions,
                tags = tags,
                imageUrl = imageUrl,
                updatedAtRemotely = updatedAtRemotely,
                updatedAtLocally = updatedAtLocally,
            )

        private fun Recipe.toDbRecipe(): DbRecipe =
            DbRecipe(
                id = id,
                title = title,
                ingredients = ingredients,
                directions = directions,
                tags = tags,
                imageUrl = imageUrl,
                updatedAtRemotely = updatedAtRemotely,
                updatedAtLocally = updatedAtLocally,
            )
    }
