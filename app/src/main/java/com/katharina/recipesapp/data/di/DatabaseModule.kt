package com.katharina.recipesapp.data.di

import android.content.Context
import androidx.room.Room
import com.katharina.recipesapp.data.db.RecipeDao
import com.katharina.recipesapp.data.db.RecipeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): RecipeDatabase =
        Room
            .databaseBuilder(
                context = context,
                klass = RecipeDatabase::class.java,
                name = "Recipes.db",
            ).build()

    @Provides
    fun providesRecipeDao(database: RecipeDatabase): RecipeDao = database.recipeDao()
}
