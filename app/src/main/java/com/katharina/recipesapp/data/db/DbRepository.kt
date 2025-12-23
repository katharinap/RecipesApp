package com.katharina.recipesapp.data.db

import com.katharina.recipesapp.data.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface DbRepository {
    suspend fun getRecipes(): Flow<List<Recipe>>

    suspend fun getRecipe(recipeId: Int): Flow<Recipe?>

    suspend fun updateRecipe(recipe: Recipe)
}

@Singleton
class DefaultDbRepository
    @Inject
    constructor(
        private val recipeDao: RecipeDao,
    ) : DbRepository {
        override suspend fun getRecipes(): Flow<List<Recipe>> = recipeDao.getRecipes().map { it.map { it.toRecipe() } }

        override suspend fun getRecipe(recipeId: Int): Flow<Recipe?> = recipeDao.getRecipe(recipeId).map { it?.toRecipe() }

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
