package com.katharina.recipesapp.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {
    @Query("SELECT * FROM shopping_list_items")
    fun getAll(): Flow<List<DbShoppingListItem>>

    @Query("SELECT * FROM shopping_list_items WHERE id = :id")
    fun getById(id: Int): Flow<DbShoppingListItem?>

    @Upsert
    suspend fun createOrUpdate(item: DbShoppingListItem)

    @Query("UPDATE shopping_list_items SET isChecked = :isChecked WHERE id = :id")
    suspend fun updateChecked(
        id: Int,
        isChecked: Boolean,
    )

    @Query("DELETE FROM shopping_list_items WHERE isChecked = 1")
    suspend fun deleteChecked()

    @Query("DELETE FROM shopping_list_items")
    suspend fun deleteAll()

    @Query("DELETE FROM shopping_list_items WHERE id = :id")
    suspend fun delete(id: Int)
}
