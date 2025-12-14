package com.katharina.recipesapp.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.katharina.recipesapp.data.network.AccessTokenInterceptor
import com.katharina.recipesapp.data.network.ApiAuthenticator
import com.katharina.recipesapp.data.network.AuthenticatedService
import com.katharina.recipesapp.data.network.Constants
import com.katharina.recipesapp.data.network.LoginService
import com.katharina.recipesapp.data.network.PublicApiService
import com.katharina.recipesapp.data.network.RefreshTokenInterceptor
import com.katharina.recipesapp.data.network.RefreshTokenService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
//    @Provides
//    fun provideBaseUrl(): String = Constants.BASE_URL
//
//    @Provides
//    @Singleton
//    fun provideRetrofit(baseUrl: String): Retrofit {
//        val networkJson =
//            Json {
//                ignoreUnknownKeys = true
//                coerceInputValues = true
//            }
//
//        return Retrofit
//            .Builder()
//            .baseUrl(baseUrl)
//            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
//            .build()
//    }
//
//    @Provides
//    @Singleton
    fun provideApiService(retrofit: Retrofit): PublicApiService = retrofit.create(PublicApiService::class.java)

    // login
    @[Provides Singleton PublicClient]
    fun provideUnauthencatedOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient
            .Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class PublicClient

    @Provides
    @Singleton
    fun provideAuthenticationApi(
        @PublicClient client: OkHttpClient,
    ): LoginService =
        Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .client(client)
            .build()
            .create(LoginService::class.java)

    // refresh token
    @[Provides Singleton RefreshTokenClient]
    fun provideTokenRefreshOkHttpClient(refreshTokenInterceptor: RefreshTokenInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient
            .Builder()
            .addInterceptor(logging)
            .addInterceptor(refreshTokenInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RefreshTokenClient

    @Provides
    @Singleton
    fun provideRefreshTokenApi(
        @RefreshTokenClient client: OkHttpClient,
    ): RefreshTokenService =
        Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .client(client)
            .build()
            .create(RefreshTokenService::class.java)

    // api calls
    @[Provides Singleton AuthenticatedClient]
    fun provideAuthenticatedOkHttpClient(
        apiAuthenticator: ApiAuthenticator,
        accessTokenInterceptor: AccessTokenInterceptor,
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient
            .Builder()
            .authenticator(apiAuthenticator)
            .addInterceptor(logging)
            .addInterceptor(accessTokenInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class AuthenticatedClient

    @Provides
    @Singleton
    fun provideAuthenticatedApi(
        @AuthenticatedClient client: OkHttpClient,
    ): AuthenticatedService =
        Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .client(client)
            .build()
            .create(AuthenticatedService::class.java)
}
