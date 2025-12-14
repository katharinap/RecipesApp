package com.katharina.recipesapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katharina.recipesapp.data.credentials.CredentialsDataStore
import com.katharina.recipesapp.data.network.ApiService
import com.katharina.recipesapp.data.network.LoginRequest
import com.katharina.recipesapp.data.network.LoginService
import com.katharina.recipesapp.data.network.RefreshTokenService
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
        private val loginService: LoginService,
        private val refreshTokenService: RefreshTokenService,
        private val apiService: ApiService,
    ) : ViewModel() {
        val userName = credentialsDataStore.userNameFlow().stateIn(viewModelScope, SharingStarted.Companion.Lazily, "")
        val password = credentialsDataStore.passwordFlow().stateIn(viewModelScope, SharingStarted.Companion.Lazily, "")
        val refreshToken = credentialsDataStore.refreshTokenFlow().stateIn(viewModelScope, SharingStarted.Companion.Lazily, "")
        val accessToken = credentialsDataStore.accessTokenFlow().stateIn(viewModelScope, SharingStarted.Companion.Lazily, "")

        fun onLogin(
            userName: String,
            password: String,
        ) {
            if (userName.isNotBlank() && password.isNotBlank()) {
                updateCredentials(userName, password)
            }

            viewModelScope.launch {
                try {
                    val response = loginService.login(LoginRequest(userName, password))

                    credentialsDataStore.updateAccessToken(response.accessToken)
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

        fun refreshTokens() {
            viewModelScope.launch {
                try {
                    val response = refreshTokenService.refreshToken(refreshToken.value)
                    credentialsDataStore.updateAccessToken(response.accessToken)
                    credentialsDataStore.updateRefreshToken(response.refreshToken)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        fun fetchRecipes() {
            viewModelScope.launch {
                try {
                    val response = apiService.getRecipes(accessToken = accessToken.value)
                    println(response)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        fun fetchRecipe42() {
            viewModelScope.launch {
                try {
                    val response = apiService.getRecipe(recipeId = 42, accessToken = accessToken.value)
                    println(response)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
