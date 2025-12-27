package com.katharina.recipesapp.data

import com.katharina.recipesapp.data.db.DbRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface ShoppingListRepository {
    suspend fun getAllShoppingListItems(): Flow<List<ShoppingListItem>>

    suspend fun getShoppingListItem(id: Int): Flow<ShoppingListItem?>

    suspend fun updateShoppingListItem(item: ShoppingListItem)

    suspend fun deleteShoppingListItem(id: Int)

    suspend fun deleteCheckedShoppingListItems()

    suspend fun deleteAllShoppingListItems()
}

@Singleton
class DefaultShoppingListRepository
    @Inject
    constructor(
        private val dbRepository: DbRepository,
    ) : ShoppingListRepository {
        override suspend fun getAllShoppingListItems(): Flow<List<ShoppingListItem>> = dbRepository.getShoppingListItemsFlow()

        override suspend fun getShoppingListItem(id: Int): Flow<ShoppingListItem?> = dbRepository.getShoppingListItemFlow(id)

        override suspend fun updateShoppingListItem(item: ShoppingListItem) = dbRepository.updateShoppingListItem(item)

        override suspend fun deleteShoppingListItem(id: Int) = dbRepository.deleteShoppingListItem(id)

        override suspend fun deleteCheckedShoppingListItems() = dbRepository.deleteCheckedShoppingListItems()

        override suspend fun deleteAllShoppingListItems() = dbRepository.deleteAllShoppingListItems()
    }
