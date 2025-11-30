package com.katharina.recipesapp.data.credentials

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CredentialsDataStore
    @Inject
    constructor(
        private val dataStore: DataStore<Credentials>,
    ) {
        fun userNameFlow(): Flow<String> =
            dataStore.data.map { credentials ->
                credentials.userName
            }

        fun passwordFlow(): Flow<String> =
            dataStore.data.map { credentials ->
                credentials.password
            }

        fun refreshTokenFlow(): Flow<String> =
            dataStore.data.map { credentials ->
                credentials.refreshToken
            }

        suspend fun updateLoginData(
            userName: String,
            password: String,
        ) {
            dataStore.updateData { credentials ->
                credentials.copy(userName = userName, password = password)
            }
        }

        suspend fun updateRefreshToken(refreshToken: String) {
            dataStore.updateData { credentials ->
                credentials.copy(refreshToken = refreshToken)
            }
        }
    }
