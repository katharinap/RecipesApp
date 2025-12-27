package com.katharina.recipesapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list_items")
data class DbShoppingListItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    var isChecked: Boolean = false,
)
