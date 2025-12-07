package com.katharina.recipesapp.ui.recipedetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.katharina.recipesapp.data.Recipe
import com.katharina.recipesapp.ui.LoadingScreen

@Composable
fun RecipeDetailsScreen(viewModel: RecipeDetailsViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        RecipeDetailsContent(uiState, Modifier.padding(innerPadding))
    }
}

@Composable
fun RecipeDetailsContent(
    uiState: RecipeDetailsViewModel.UiState,
    modifier: Modifier = Modifier,
) {
    if (uiState.isLoading) {
        LoadingScreen(modifier = modifier)
    } else if (uiState.recipe != null) {
        RecipeDetails(recipe = uiState.recipe, modifier = modifier)
    } else {
        Text(text = "Error", style = MaterialTheme.typography.headlineLarge)
    }
}

@Composable
fun RecipeDetails(
    recipe: Recipe?,
    modifier: Modifier,
) {
    Column(modifier = modifier) {
        if (recipe == null) {
            Text(text = "Recipe not found", style = MaterialTheme.typography.headlineLarge)
        } else {
            Text(text = recipe.title, style = MaterialTheme.typography.headlineLarge)

            Text(text = "Ingredients", style = MaterialTheme.typography.headlineMedium)
            recipe.ingredients.forEach { ingredient ->
                Text(text = "- $ingredient", style = MaterialTheme.typography.bodyLarge)
            }

            Text(text = "Description", style = MaterialTheme.typography.headlineMedium)
            Text(text = recipe.description, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
