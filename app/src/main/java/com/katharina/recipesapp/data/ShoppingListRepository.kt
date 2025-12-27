package com.katharina.recipesapp.data

import com.katharina.recipesapp.data.db.DbRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface ShoppingListRepository {
    suspend fun getAllShoppingListItems(): Flow<List<ShoppingListItem>>

    suspend fun updateShoppingListItem(item: ShoppingListItem)

    suspend fun deleteCheckedShoppingListItems()

    suspend fun deleteAllShoppingListItems()

    suspend fun addIngredient(ingredient: String)
}

@Singleton
class DefaultShoppingListRepository
    @Inject
    constructor(
        private val dbRepository: DbRepository,
    ) : ShoppingListRepository {
        override suspend fun getAllShoppingListItems(): Flow<List<ShoppingListItem>> = dbRepository.getShoppingListItemsFlow()

        override suspend fun updateShoppingListItem(item: ShoppingListItem) = dbRepository.updateShoppingListItem(item)

        override suspend fun deleteCheckedShoppingListItems() = dbRepository.deleteCheckedShoppingListItems()

        override suspend fun deleteAllShoppingListItems() = dbRepository.deleteAllShoppingListItems()

        override suspend fun addIngredient(ingredient: String) = dbRepository.addShoppingListItem(ingredient)
    }
