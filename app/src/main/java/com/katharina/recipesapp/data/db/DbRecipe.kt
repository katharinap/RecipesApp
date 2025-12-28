package com.katharina.recipesapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "recipes")
data class DbRecipe(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val title: String,
    val tags: List<String>,
    val ingredients: List<String>,
    val directions: List<String>,
    val imageUrl: String?,
    val updatedAtRemotely: LocalDateTime?,
    val updatedAtLocally: LocalDateTime?,
    val language: String?,
)
