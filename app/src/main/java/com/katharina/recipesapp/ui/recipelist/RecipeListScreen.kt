package com.katharina.recipesapp.ui.recipelist

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.katharina.recipesapp.data.Recipe
import com.katharina.recipesapp.ui.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(
    viewModel: RecipeListViewModel,
    onRecipeSelected: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors =
                    topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                title = {
                    Text("My Recipes")
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Bottom app bar",
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.updateRecipes() }) {
                Icon(Icons.Default.Refresh, contentDescription = "Update Recipes")
            }
        },
    ) { innerPadding ->
        Column(
//            modifier = Modifier.padding(innerPadding)
        ) {
            when (uiState) {
                is RecipeListViewModel.UiState.Loading -> {
                    LoadingScreen(Modifier.padding(innerPadding))
                }

                is RecipeListViewModel.UiState.Success -> {
                    RecipeList(
                        uiState = uiState as RecipeListViewModel.UiState.Success,
                        onRecipeSelected = onRecipeSelected,
                        innerPadding = innerPadding,
                    )
                }
            }
        }

        if (viewModel.message.isNotEmpty()) {
            val toast = Toast.makeText(LocalContext.current, viewModel.message, Toast.LENGTH_LONG)
            toast.show()
        }
    }
}

@Composable
fun RecipeList(
    uiState: RecipeListViewModel.UiState.Success,
    onRecipeSelected: (Int) -> Unit,
    innerPadding: PaddingValues,
) {
    LazyColumn(contentPadding = innerPadding) {
        items(items = uiState.recipes) { recipe ->
            RecipeListItem(recipe = recipe, onRecipeSelected = onRecipeSelected)
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
