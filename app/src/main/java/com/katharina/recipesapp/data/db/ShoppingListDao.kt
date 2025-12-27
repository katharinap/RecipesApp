package com.katharina.recipesapp.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {
    @Query("SELECT * FROM shopping_list_items")
    fun getAll(): Flow<List<DbShoppingListItem>>

    @Query("SELECT * FROM shopping_list_items WHERE name = :name")
    suspend fun getByName(name: String): DbShoppingListItem?

    @Upsert
    suspend fun createOrUpdate(item: DbShoppingListItem)

    @Query("DELETE FROM shopping_list_items WHERE isChecked = 1")
    suspend fun deleteChecked()

    @Query("DELETE FROM shopping_list_items")
    suspend fun deleteAll()
}
