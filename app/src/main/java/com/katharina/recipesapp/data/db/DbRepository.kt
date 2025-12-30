package com.katharina.recipesapp.data.db

import com.katharina.recipesapp.data.Recipe
import com.katharina.recipesapp.data.ShoppingListItem
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

    suspend fun getRecipe(recipeId: Int): Recipe?

    suspend fun updateRecipe(recipe: Recipe)

    suspend fun getShoppingListItemsFlow(): Flow<List<ShoppingListItem>>

    suspend fun updateShoppingListItem(item: ShoppingListItem)

    suspend fun deleteCheckedShoppingListItems()

    suspend fun deleteAllShoppingListItems()

    suspend fun addShoppingListItem(ingredient: String)
}

@Singleton
class DefaultDbRepository
    @Inject
    constructor(
        private val recipeDao: RecipeDao,
        private val shoppingListDao: ShoppingListDao,
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

        override suspend fun getRecipe(recipeId: Int): Recipe? = recipeDao.getRecipe(recipeId)?.toRecipe()

        override suspend fun updateRecipe(recipe: Recipe) {
            withContext(Dispatchers.IO) {
                recipeDao.upsertRecipe(recipe.toDbRecipe())
            }
        }

        override suspend fun getShoppingListItemsFlow(): Flow<List<ShoppingListItem>> =
            shoppingListDao.getAll().map { list ->
                list.map {
                    it.toShoppingListItem()
                }
            }

        override suspend fun updateShoppingListItem(item: ShoppingListItem) = shoppingListDao.createOrUpdate(item.toDbShoppingListItem())

        override suspend fun deleteCheckedShoppingListItems() = shoppingListDao.deleteChecked()

        override suspend fun deleteAllShoppingListItems() = shoppingListDao.deleteAll()

        override suspend fun addShoppingListItem(ingredient: String) {
            val existingItem = shoppingListDao.getByName(ingredient)
            if (existingItem == null) {
                shoppingListDao.createOrUpdate(DbShoppingListItem(name = ingredient))
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
                language = language,
                starred = starred,
                source = source,
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
                language = language,
                starred = starred,
                source = source,
            )

        private fun DbShoppingListItem.toShoppingListItem(): ShoppingListItem =
            ShoppingListItem(id = id, name = name, isChecked = isChecked)

        private fun ShoppingListItem.toDbShoppingListItem(): DbShoppingListItem =
            DbShoppingListItem(id = id, name = name, isChecked = isChecked)
    }
