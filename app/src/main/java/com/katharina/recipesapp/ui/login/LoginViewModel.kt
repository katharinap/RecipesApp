package com.katharina.recipesapp.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katharina.recipesapp.data.RecipeRepository
import com.katharina.recipesapp.data.credentials.CredentialsDataStore
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
        private val recipeRepository: RecipeRepository,
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
                message = "Credentials updated"
            }
        }

        fun updateRecipe(recipeId: Int) {
            viewModelScope.launch {
                message = recipeRepository.fetchRecipe(recipeId)
            }
        }
    }
