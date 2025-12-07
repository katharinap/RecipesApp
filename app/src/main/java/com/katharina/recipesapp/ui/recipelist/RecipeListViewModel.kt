package com.katharina.recipesapp.ui.recipelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katharina.recipesapp.data.Recipe
import com.katharina.recipesapp.data.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel
    @Inject
    constructor(
        private val recipeRepository: RecipeRepository,
    ) : ViewModel() {
        data class UiState(
            val isLoading: Boolean,
            val recipes: List<Recipe>,
        )

        val uiState: StateFlow<UiState> =
            recipeRepository
                .getAllRecipesStream()
                .map {
                    UiState(
                        isLoading = false,
                        recipes = it,
                    )
                }.stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(5000),
                    UiState(isLoading = true, recipes = emptyList()),
                )
    }
