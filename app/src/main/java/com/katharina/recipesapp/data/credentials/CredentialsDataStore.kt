package com.katharina.recipesapp.data.credentials

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Credentials> by dataStore(
    fileName = "credentials.json",
    serializer = CredentialsSerializer,
)

class CredentialsDataStore(
    private val context: Context,
) {
    fun userNameFlow(): Flow<String> =
        context.dataStore.data.map { credentials ->
            credentials.userName
        }

    fun passwordFlow(): Flow<String> =
        context.dataStore.data.map { credentials ->
            credentials.password
        }

    fun refreshTokenFlow(): Flow<String> =
        context.dataStore.data.map { credentials ->
            credentials.refreshToken
        }

    suspend fun updateUserName(username: String) {
        context.dataStore.updateData { credentials ->
            credentials.copy(userName = username)
        }
    }

    suspend fun updatePassword(password: String) {
        context.dataStore.updateData { credentials ->
            credentials.copy(password = password)
        }
    }

    suspend fun updateRefreshToken(refreshToken: String) {
        context.dataStore.updateData { credentials ->
            credentials.copy(refreshToken = refreshToken)
        }
    }
}
