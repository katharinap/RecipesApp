package com.katharina.recipesapp.ui.recipelist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.katharina.recipesapp.ui.LoadingScreen
import com.katharina.recipesapp.ui.theme.RecipesAppTheme
import com.katharina.recipesapp.ui.utils.RecipeBottomAppBar
import com.katharina.recipesapp.ui.utils.RecipeList

@Composable
fun RecipeListScreen(
    viewModel: RecipeListViewModel,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onRecipeSelected: (Int) -> Unit,
    navController: NavHostController,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RecipeListTopBar(
                query = viewModel.query,
                onSearch = { viewModel.searchRecipes(it) },
            )
        },
        bottomBar = {
            RecipeBottomAppBar(navController = navController)
        },
        floatingActionButton = { RecipeListRefreshButton(onClick = viewModel::updateRecipes) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            when (uiState) {
                is RecipeListViewModel.UiState.Loading -> {
                    LoadingScreen(Modifier.padding(innerPadding))
                }

                is RecipeListViewModel.UiState.Success -> {
                    val recipes = (uiState as RecipeListViewModel.UiState.Success).recipes
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListTopBar(
    query: String = "",
    onSearch: (String) -> Unit = {},
) {
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onSearch,
                onSearch = onSearch,
                expanded = false,
                onExpandedChange = {},
                placeholder = { Text("Search") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                },
                trailingIcon = {
                    IconButton(onClick = { onSearch("") }) {
                        Icon(Icons.Default.Clear, "Clear")
                    }
                },
            )
        },
        expanded = false,
        onExpandedChange = { },
    ) { }
}

@Composable
fun RecipeListRefreshButton(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(Icons.Default.Refresh, contentDescription = "Update Recipes")
    }
}

@PreviewLightDark
@Composable
fun RecipeListTopBarOldPreview() {
    RecipesAppTheme {
        RecipeListTopBar()
    }
}
