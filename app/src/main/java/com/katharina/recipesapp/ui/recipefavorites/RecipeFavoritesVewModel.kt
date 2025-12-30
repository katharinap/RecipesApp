package com.katharina.recipesapp.ui.recipefavorites

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katharina.recipesapp.data.Recipe
import com.katharina.recipesapp.data.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeFavoritesVewModel
    @Inject
    constructor(
        private val recipeRepository: RecipeRepository,
    ) : ViewModel() {
        sealed class UiState {
            object Loading : UiState()

            data class Success(
                val recipes: List<Recipe>,
            ) : UiState()
        }

        var message by mutableStateOf("")
            private set

        private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
        val uiState: StateFlow<UiState> = _uiState

        init {
            loadRecipes()
        }

        private fun loadRecipes() {
            viewModelScope.launch {
                try {
                    recipeRepository.getAllRecipes().collect { result ->
                        _uiState.value =
                            UiState.Success(recipes = result.filter { recipe -> recipe.starred })
                    }
                } catch (exception: Error) {
                    showSnackbarMessage(exception.message ?: "Unknown error")
                    println(exception.message)
                }
            }
        }

        fun snackbarMessageShown() {
            message = ""
        }

        private fun showSnackbarMessage(text: String) {
            message = text
        }
    }
