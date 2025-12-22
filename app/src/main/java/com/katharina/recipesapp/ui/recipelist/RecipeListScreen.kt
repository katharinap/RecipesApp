package com.katharina.recipesapp.ui.recipelist

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.katharina.recipesapp.R
import com.katharina.recipesapp.data.Recipe
import com.katharina.recipesapp.ui.LoadingScreen
import com.katharina.recipesapp.ui.theme.RecipesAppTheme

@Composable
fun RecipeListScreen(
    viewModel: RecipeListViewModel,
    onRecipeSelected: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { RecipeListTopBar() },
        bottomBar = { RecipeListBottomBar() },
        floatingActionButton = { RecipeListRefreshButton(onClick = viewModel::updateRecipes) },
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
            val toast = Toast.makeText(LocalContext.current, viewModel.message, Toast.LENGTH_LONG)
            toast.show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListTopBar() {
    TopAppBar(
        colors =
            topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
        title = {
            Row {
                Icon(
                    painter = painterResource(R.drawable.baseline_cookie_24),
                    contentDescription = "Cookie",
                )
                Text(
                    text = "My Recipes",
                    modifier =
                        Modifier.padding(
                            start = 10.dp,
                        ),
                )
            }
        },
    )
}

@Composable
fun RecipeListBottomBar() {
    var filter by remember { mutableStateOf("") }

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = filter,
            onValueChange = { filter = it },
            label = { Text("Filter") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Filter") },
        )
    }
}

@Composable
fun RecipeListRefreshButton(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(Icons.Default.Refresh, contentDescription = "Update Recipes")
    }
}

@Composable
fun RecipeList(
    recipes: List<Recipe>,
    onRecipeSelected: (Int) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background)) {
        items(items = recipes) { recipe ->
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
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@PreviewLightDark
@Composable
fun RecipeListTopBarPreview() {
    RecipesAppTheme {
        RecipeListTopBar()
    }
}

@PreviewLightDark
@Composable
fun RecipeListPreview() {
    val recipes =
        listOf<Recipe>(
            Recipe(
                id = 1,
                title = "Recipe 1",
            ),
            Recipe(
                id = 2,
                title = "Recipe 2",
            ),
            Recipe(
                id = 3,
                title = "Recipe 3",
            ),
        )

    RecipesAppTheme {
        RecipeList(
            recipes = recipes,
            onRecipeSelected = {},
        )
    }
}

@PreviewLightDark
@Composable
fun RecipeListBottomBarPreview() {
    RecipesAppTheme {
        RecipeListBottomBar()
    }
}
