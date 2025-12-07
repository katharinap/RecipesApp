package com.katharina.recipesapp.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katharina.recipesapp.data.credentials.CredentialsDataStore
import com.katharina.recipesapp.data.network.ApiService
import com.katharina.recipesapp.data.network.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val credentialsDataStore: CredentialsDataStore,
        private val apiService: ApiService,
    ) : ViewModel() {
        val userName = credentialsDataStore.userNameFlow().stateIn(viewModelScope, SharingStarted.Companion.Lazily, "")
        val password = credentialsDataStore.passwordFlow().stateIn(viewModelScope, SharingStarted.Companion.Lazily, "")
        val refreshToken = credentialsDataStore.refreshTokenFlow().stateIn(viewModelScope, SharingStarted.Companion.Lazily, "")
        var accessToken by mutableStateOf("")
            private set

        fun onLogin(
            userName: String,
            password: String,
        ) {
            if (userName.isNotBlank() && password.isNotBlank()) {
                updateCredentials(userName, password)
            }

            viewModelScope.launch {
                try {
                    val response = apiService.login(LoginRequest(userName, password))

                    accessToken = response.accessToken
                    credentialsDataStore.updateRefreshToken(response.refreshToken)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        fun updateCredentials(
            userName: String,
            password: String,
        ) {
            viewModelScope.launch {
                credentialsDataStore.updateLoginData(userName, password)
            }
        }
    }
