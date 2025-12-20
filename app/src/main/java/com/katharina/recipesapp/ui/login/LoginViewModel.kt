package com.katharina.recipesapp.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katharina.recipesapp.data.credentials.CredentialsDataStore
import com.katharina.recipesapp.data.network.NetworkRepository
import com.katharina.recipesapp.data.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val credentialsDataStore: CredentialsDataStore,
        private val networkRepository: NetworkRepository,
    ) : ViewModel() {
        val userName = credentialsDataStore.userNameFlow().stateIn(viewModelScope, SharingStarted.Companion.Lazily, "")
        val password = credentialsDataStore.passwordFlow().stateIn(viewModelScope, SharingStarted.Companion.Lazily, "")
        val refreshToken = credentialsDataStore.refreshTokenFlow().stateIn(viewModelScope, SharingStarted.Companion.Lazily, "")
        val accessToken = credentialsDataStore.accessTokenFlow().stateIn(viewModelScope, SharingStarted.Companion.Lazily, "")

        var message by mutableStateOf("")
            private set

        fun updateCredentials(
            userName: String,
            password: String,
        ) {
            viewModelScope.launch {
                credentialsDataStore.updateLoginData(userName, password)
            }
        }

        fun fetchRecipes() {
            viewModelScope.launch {
                val response = networkRepository.getRecipes()
                if (response is NetworkResult.Success) {
                    message = "Successfully fetched ${response.data.size} recipes"
                    println(response)
                } else {
                    message = (response as NetworkResult.Error).exception.message ?: "Unknown error"
                }
            }
        }

        @OptIn(ExperimentalTime::class)
        fun fetchRecipe(recipeId: Int) {
            viewModelScope.launch {
                val response = networkRepository.getRecipe(recipeId = recipeId)
                if (response is NetworkResult.Success) {
                    println(response)
                    message = "Successfully fetched recipe ${response.data.title}"
                } else {
                    message = (response as NetworkResult.Error).exception.message ?: "Unknown error"
                }
            }
        }
    }
