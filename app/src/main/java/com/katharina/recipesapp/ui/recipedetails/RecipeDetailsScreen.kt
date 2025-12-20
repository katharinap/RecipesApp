package com.katharina.recipesapp.ui.recipedetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.katharina.recipesapp.ui.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(viewModel: RecipeDetailsViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        RecipeDetailsViewModel.UiState.Loading -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
            ) { innerPadding ->
                LoadingScreen(Modifier.padding(innerPadding))
            }
        }

        is RecipeDetailsViewModel.UiState.Success -> {
            val recipe = (uiState as RecipeDetailsViewModel.UiState.Success).recipe

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = { Text(text = recipe.title) },
                        modifier = Modifier.fillMaxWidth(),
                        colors =
                            topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                    )
                },
                bottomBar = {
                    BottomAppBar(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary,
                        windowInsets = BottomAppBarDefaults.windowInsets,
                    ) {
                        Column {
                            Text(
                                text = "Last updated remotely: ${recipe.updatedAtRemotely}",
                                style = MaterialTheme.typography.bodySmall,
                            )
                            Text(
                                text = "Last updated locally: ${recipe.updatedAtLocally}",
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                },
                floatingActionButton = {
                    if (recipe.needsUpdate()) {
                        FloatingActionButton(
                            onClick = { viewModel.updateRecipe() },
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Update Recipe")
                        }
                    }
                },
            ) { innerPadding ->

                RecipeDetails(
                    uiState as RecipeDetailsViewModel.UiState.Success,
                    Modifier.padding(innerPadding),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetails(
    uiState: RecipeDetailsViewModel.UiState.Success,
    modifier: Modifier,
) {
    val recipe = uiState.recipe
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .fillMaxWidth()
                .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalAlignment = Alignment.End,
    ) {
        Row(modifier = Modifier.padding(4.dp)) {
            recipe.tags.forEach { tag ->
                Box(modifier = Modifier.padding(4.dp)) {
                    Text(text = tag, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        ) {
            Text(text = "Ingredients", style = MaterialTheme.typography.headlineMedium)
            recipe.ingredients.forEach { ingredient ->
                Text(text = "- $ingredient", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        ) {
            Text(text = "Directions", style = MaterialTheme.typography.headlineMedium)
            recipe.directions.forEach { direction ->
                Text(text = "- $direction", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
