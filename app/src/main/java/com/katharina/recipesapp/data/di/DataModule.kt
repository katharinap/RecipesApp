package com.katharina.recipesapp.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.katharina.recipesapp.data.credentials.Credentials
import com.katharina.recipesapp.data.credentials.CredentialsSerializer
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
    fun provideCredentialsDataStore(@ApplicationContext context: Context): DataStore<Credentials> {
        return context.dataStore
    }
}
