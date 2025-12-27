package com.katharina.recipesapp.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.katharina.recipesapp.data.DefaultRecipesRepository
import com.katharina.recipesapp.data.DefaultShoppingListRepository
import com.katharina.recipesapp.data.RecipeRepository
import com.katharina.recipesapp.data.ShoppingListRepository
import com.katharina.recipesapp.data.credentials.Credentials
import com.katharina.recipesapp.data.credentials.CredentialsSerializer
import com.katharina.recipesapp.data.db.DbRepository
import com.katharina.recipesapp.data.db.DefaultDbRepository
import com.katharina.recipesapp.data.network.DefaultNetworkRepository
import com.katharina.recipesapp.data.network.NetworkRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Credentials> by dataStore(
    fileName = "credentials.json",
    serializer = CredentialsSerializer,
)

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideCredentialsDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Credentials> = context.dataStore
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RecipeRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRecipeRepository(repository: DefaultRecipesRepository): RecipeRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindNetworkRepository(repository: DefaultNetworkRepository): NetworkRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DbRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindDbRepository(repository: DefaultDbRepository): DbRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ShoppingListRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindShoppingListRepository(repository: DefaultShoppingListRepository): ShoppingListRepository
}
