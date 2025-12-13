package com.katharina.recipesapp.ui.recipelist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.katharina.recipesapp.data.Recipe

@Composable
fun RecipeListScreen(
    viewModel: RecipeListViewModel,
    onRecipeSelected: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(uiState.recipes) { recipe ->
                RecipeListItem(recipe = recipe, onRecipeSelected = onRecipeSelected)
            }
        }
    }
}

@Composable
fun RecipeListItem(
    recipe: Recipe,
    modifier: Modifier = Modifier,
    onRecipeSelected: (Int) -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = recipe.title,
            modifier = Modifier.clickable { onRecipeSelected(recipe.id) },
        )
    }
}
