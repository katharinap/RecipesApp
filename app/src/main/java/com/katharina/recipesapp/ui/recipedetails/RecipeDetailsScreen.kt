package com.katharina.recipesapp.ui.recipedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.katharina.recipesapp.R
import com.katharina.recipesapp.data.Recipe
import com.katharina.recipesapp.ui.LoadingScreen
import com.katharina.recipesapp.ui.theme.RecipesAppTheme
import com.katharina.recipesapp.ui.utils.LinkShare
import com.katharina.recipesapp.ui.utils.LinkShareDisabled
import com.katharina.recipesapp.ui.utils.RecipeBottomAppBar
import com.katharina.recipesapp.ui.utils.TagItem
import java.time.LocalDateTime

@Composable
fun RecipeDetailsScreen(
    viewModel: RecipeDetailsViewModel,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navController: NavHostController,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        RecipeDetailsViewModel.UiState.Loading -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    RecipeBottomAppBar(navController = navController)
                },
            ) { innerPadding ->
                LoadingScreen(Modifier.padding(innerPadding))
            }
        }

        is RecipeDetailsViewModel.UiState.Success -> {
            val recipe = (uiState as RecipeDetailsViewModel.UiState.Success).recipe

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    RecipeDetailsTopBar(
                        recipe = recipe,
                        onToggleStarred = viewModel::toggleStarred,
                    )
                },
                bottomBar = {
                    RecipeBottomAppBar(navController = navController)
                },
                floatingActionButton = { RecipeDetailsRefreshButton(onClick = viewModel::updateRecipe) },
                snackbarHost = { SnackbarHost(snackbarHostState) },
            ) { innerPadding ->

                RecipeDetails(
                    recipe = recipe,
                    onAddIngredientsToShoppingList = viewModel::addIngredientsToShoppingList,
                    Modifier.padding(innerPadding),
                )

                if (viewModel.message.isNotEmpty()) {
                    LaunchedEffect(snackbarHostState, viewModel, viewModel.message) {
                        snackbarHostState.showSnackbar(viewModel.message)
                        viewModel.snackbarMessageShown()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetails(
    recipe: Recipe,
    onAddIngredientsToShoppingList: (Recipe) -> Unit = {},
    modifier: Modifier,
) {
    Column(
        modifier =
            modifier.then(
                Modifier
                    .padding(8.dp)
                    .background(color = MaterialTheme.colorScheme.surface)
                    .verticalScroll(rememberScrollState()),
            ),
    ) {
        Column(
            modifier = Modifier.padding(4.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (recipe.imageUrl == null) {
                Text(
                    text = "\uD83C\uDF72",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            } else {
                recipe.getRemoteImageUrl()?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = "Recipe Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(300.dp),
                        alignment = Alignment.Center,
                    )
                }
            }
        }

        if (recipe.tags.isNotEmpty()) {
            Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.outline_tag_24),
                    contentDescription = "Tags",
                    tint = MaterialTheme.colorScheme.tertiary,
                )
                recipe.tags.forEach { tag ->
                    TagItem(tag = tag)
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = if (recipe.isGerman()) stringResource(R.string.ingredients_de) else stringResource(R.string.ingredients_en),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            IconButton(
                onClick = { onAddIngredientsToShoppingList(recipe) },
                colors =
                    IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary,
                    ),
            ) {
                Icon(painter = painterResource(R.drawable.outline_list_alt_add_24), contentDescription = "Add to Shopping List")
            }
        }
        recipe.ingredients.forEach { ingredient ->
            Text(
                text = "• $ingredient",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        Text(
            text = if (recipe.isGerman()) stringResource(R.string.direction_de) else stringResource(R.string.direction_en),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 8.dp),
        )
        recipe.directions.forEach { direction ->
            Text(
                text = direction,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsTopBar(
    recipe: Recipe,
    onToggleStarred: () -> Unit,
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_cookie_24),
                        contentDescription = "Cookie",
                    )
                    Text(
                        text = recipe.title,
                        modifier =
                            Modifier.padding(
                                start = 10.dp,
                            ),
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onToggleStarred) {
                        Icon(
                            painter =
                                if (recipe.starred) {
                                    painterResource(R.drawable.baseline_star_24)
                                } else {
                                    painterResource(R.drawable.outline_star_24)
                                },
                            contentDescription = if (recipe.starred) "Remove from favorites" else "Add to favorites",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }

                    if (recipe.source != null && recipe.source.startsWith("http")) {
                        LinkShare(link = recipe.source, context = LocalContext.current)
                    } else {
                        LinkShareDisabled()
                    }
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors =
            topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
    )
}

@Composable
fun RecipeDetailsRefreshButton(onClick: () -> Unit) {
    val showButton = false
    if (showButton) {
        FloatingActionButton(onClick = onClick) {
            Icon(Icons.Default.Refresh, contentDescription = "Update Recipe")
        }
    }
}

@PreviewLightDark
@Composable
fun RecipeDetailsTopBarPreview() {
    val recipe =
        Recipe(
            id = 1,
            title = "Recipe 1",
        )
    RecipesAppTheme {
        RecipeDetailsTopBar(
            recipe = recipe,
            onToggleStarred = {},
        )
    }
}

@PreviewLightDark
@Composable
fun RecipeDetailsPreview() {
    val recipe =
        Recipe(
            id = 1,
            title = "Recipe 1",
            ingredients = listOf("Ingredient 1", "Ingredient 2"),
            directions = listOf("Direction 1", "Direction 2"),
            tags = listOf("tag1", "tag2"),
            updatedAtRemotely = LocalDateTime.now(),
            updatedAtLocally = LocalDateTime.now(),
            language = "german",
            starred = false,
        )
    RecipesAppTheme {
        RecipeDetails(recipe = recipe, modifier = Modifier.padding(4.dp))
    }
}
