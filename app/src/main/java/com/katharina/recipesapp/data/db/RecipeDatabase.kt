package com.katharina.recipesapp.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [DbRecipe::class],
    exportSchema = true,
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
    ],
)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}
