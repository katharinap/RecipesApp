package com.katharina.recipesapp.ui.recipedetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katharina.recipesapp.data.Ingredient
import com.katharina.recipesapp.data.Recipe
import com.katharina.recipesapp.data.RecipeRepository
import com.katharina.recipesapp.data.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel
    @Inject
    constructor(
        private val recipeRepository: RecipeRepository,
        private val shoppingListRepository: ShoppingListRepository,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        sealed class UiState {
            object Loading : UiState()

            data class Success(
                val recipe: Recipe,
            ) : UiState()
        }

        var message by mutableStateOf("")
            private set

        var keepScreenOn by mutableStateOf(false)

        private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
        val uiState: StateFlow<UiState> = _uiState

        init {
            loadRecipe()
        }

        fun loadRecipe() {
            val recipeId = savedStateHandle.get<Int>("recipeId")
            if (recipeId != null) {
                viewModelScope.launch {
                    try {
                        recipeRepository.getRecipeFlow(recipeId).collect { recipe ->
                            if (recipe != null) {
                                _uiState.value = UiState.Success(recipe)
                            } else {
                                showSnackbarMessage("Failed to get recipe with id $recipeId")
                            }
                        }
                    } catch (exception: Error) {
                        showSnackbarMessage(exception.message ?: "Unknown error")
                        println(exception.message)
                    }
                }
            } else {
                showSnackbarMessage("Missing recipe id")
            }
        }

        fun updateRecipe() {
            viewModelScope.launch {
                val recipeId = savedStateHandle.get<Int>("recipeId")
                if (recipeId != null) {
                    val text = recipeRepository.fetchRecipe(recipeId)
                    showSnackbarMessage(text)
                } else {
                    showSnackbarMessage("Missing recipe id")
                }
            }
        }

        fun addIngredientsToShoppingList(recipe: Recipe) {
            viewModelScope.launch {
                recipe.ingredients.forEach { ingredient ->
                    if (!Ingredient.isHeading(ingredient)) {
                        shoppingListRepository.addIngredient(ingredient)
                    }
                }
                showSnackbarMessage("Added ingredients to shopping list")
            }
        }

        fun toggleStarred() {
            viewModelScope.launch {
                val recipeId = savedStateHandle.get<Int>("recipeId")
                if (recipeId != null) {
                    val recipe = recipeRepository.getRecipe(recipeId)
                    if (recipe != null) {
                        recipeRepository.updateRecipe(recipe.copy(starred = !recipe.starred))
                    } else {
                        showSnackbarMessage("Invalid recipe id $recipeId")
                    }
                } else {
                    showSnackbarMessage("Missing recipe id")
                }
            }
        }

        fun snackbarMessageShown() {
            message = ""
        }

        private fun showSnackbarMessage(text: String) {
            message = text
        }

        override fun onCleared() {
            super.onCleared()
            keepScreenOn = false
        }

        fun toggleKeepScreenOn() {
            keepScreenOn = !keepScreenOn
        }
    }
