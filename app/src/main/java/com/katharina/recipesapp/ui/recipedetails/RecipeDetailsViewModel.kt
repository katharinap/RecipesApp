package com.katharina.recipesapp.ui.recipedetails

import androidx.lifecycle.SavedStateHandle
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
class RecipeDetailsViewModel
    @Inject
    constructor(
        private val recipeRepository: RecipeRepository,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        data class UiState(
            val isLoading: Boolean,
            val recipe: Recipe?,
        )

        private val recipeId: Int = savedStateHandle.get<Int>("recipeId")!!

        val uiState: StateFlow<UiState> =
            recipeRepository
                .getRecipeById(id = recipeId)
                .map { recipe ->
                    UiState(
                        isLoading = false,
                        recipe = recipe,
                    )
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue =
                        UiState(
                            isLoading = true,
                            recipe = null,
                        ),
                )
    }
