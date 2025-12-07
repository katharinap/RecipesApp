package com.katharina.recipesapp.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.katharina.recipesapp.data.network.ApiService
import com.katharina.recipesapp.data.network.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideBaseUrl(): String = Constants.BASE_URL

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit {
        val networkJson =
            Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }

        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}
