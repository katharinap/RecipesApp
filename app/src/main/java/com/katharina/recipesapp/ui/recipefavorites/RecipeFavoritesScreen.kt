package com.katharina.recipesapp.ui.recipefavorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.katharina.recipesapp.ui.LoadingScreen
import com.katharina.recipesapp.ui.utils.RecipeBottomAppBar
import com.katharina.recipesapp.ui.utils.RecipeList

@Composable
fun RecipeFavoritesScreen(
    viewModel: RecipeFavoritesVewModel,
    navController: NavHostController,
    onRecipeSelected: (Int) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            RecipeBottomAppBar(navController = navController)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when (uiState) {
                is RecipeFavoritesVewModel.UiState.Loading -> {
                    LoadingScreen(Modifier.padding(innerPadding))
                }

                is RecipeFavoritesVewModel.UiState.Success -> {
                    val recipes = (uiState as RecipeFavoritesVewModel.UiState.Success).recipes
                    RecipeList(
                        recipes = recipes,
                        onRecipeSelected = onRecipeSelected,
                    )
                }
            }
        }

        if (viewModel.message.isNotEmpty()) {
            LaunchedEffect(snackbarHostState, viewModel, viewModel.message) {
                snackbarHostState.showSnackbar(viewModel.message)
                viewModel.snackbarMessageShown()
            }
        }
    }
}
