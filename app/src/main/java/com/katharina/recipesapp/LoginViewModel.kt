package com.katharina.recipesapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    ) : ViewModel() {
        val userName = credentialsDataStore.userNameFlow().stateIn(viewModelScope, SharingStarted.Lazily, "")
        val password = credentialsDataStore.passwordFlow().stateIn(viewModelScope, SharingStarted.Lazily, "")
        val refreshToken = credentialsDataStore.refreshTokenFlow().stateIn(viewModelScope, SharingStarted.Lazily, "")

        fun onLogin(
            userName: String,
            password: String,
        ) {
            viewModelScope.launch {
                credentialsDataStore.updateLoginData(userName, password)
            }
        }
    }
