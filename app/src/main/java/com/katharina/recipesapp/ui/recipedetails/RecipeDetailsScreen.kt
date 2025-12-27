package com.katharina.recipesapp.ui.recipedetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.katharina.recipesapp.R
import com.katharina.recipesapp.data.Recipe
import com.katharina.recipesapp.ui.LoadingScreen
import com.katharina.recipesapp.ui.theme.RecipesAppTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun RecipeDetailsScreen(
    viewModel: RecipeDetailsViewModel,
    onGoToShoppingList: () -> Unit,
    onGoToRecipeList: () -> Unit,
) {
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
                    RecipeDetailsTopBar(recipe = recipe, onGoToShoppingList = onGoToShoppingList, onGoToRecipeList = onGoToRecipeList)
                },
//                bottomBar = {
//                    RecipeDetailsBottomBar(recipe = recipe)
//                },
                floatingActionButton = {
                    RecipeDetailsRefreshButton(
                        recipe = recipe,
                        onRefresh = viewModel::updateRecipe,
                    )
                },
            ) { innerPadding ->

                RecipeDetails(
                    recipe = recipe,
                    Modifier.padding(innerPadding),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetails(
    recipe: Recipe,
    modifier: Modifier,
) {
    Column(
        modifier =
            modifier.then(
                Modifier
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
                Image(
                    painter = painterResource(R.drawable.recipe_default_350),
                    contentDescription = "Default Recipe Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(300.dp),
                    alignment = Alignment.Center,
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
            Row(modifier = Modifier.padding(4.dp)) {
                Icon(
                    painter = painterResource(R.drawable.outline_tag_24),
                    contentDescription = "Tags",
                    tint = MaterialTheme.colorScheme.tertiary,
                )
                recipe.tags.forEach { tag ->
                    Box(
                        modifier =
                            Modifier
                                .padding(4.dp)
                                .clip(RoundedCornerShape(percent = 25))
                                .background(color = MaterialTheme.colorScheme.tertiaryContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = tag,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.padding(4.dp),
                        )
                    }
                }
            }
        }

        Text(
            text = "Ingredients",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        recipe.ingredients.forEach { ingredient ->
            Text(text = "- $ingredient", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
        }

        Text(
            text = "Directions",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 8.dp),
        )
        recipe.directions.forEach { direction ->
            Text(text = direction, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsTopBar(
    recipe: Recipe,
    onGoToShoppingList: () -> Unit,
    onGoToRecipeList: () -> Unit,
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row {
                    Icon(
                        painter = painterResource(R.drawable.baseline_cookie_24),
                        contentDescription = "Cookie",
                        modifier = Modifier.clickable { onGoToRecipeList() },
                    )
                    Text(
                        text = recipe.title,
                        modifier =
                            Modifier.padding(
                                start = 10.dp,
                            ),
                    )
                }
                Icon(
                    painter = painterResource(R.drawable.baseline_list_alt_24),
                    contentDescription = "List",
                    modifier = Modifier.clickable { onGoToShoppingList() }.padding(end = 10.dp),
                )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsBottomBar(recipe: Recipe) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
        windowInsets = BottomAppBarDefaults.windowInsets,
    ) {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        Column {
            Text(
                text = "Last updated remotely: ${recipe.updatedAtRemotely?.format(formatter)}",
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = "Last updated locally: ${recipe.updatedAtLocally?.format(formatter)}",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
fun RecipeDetailsRefreshButton(
    recipe: Recipe,
    onRefresh: () -> Unit,
) {
    val show = true // recipe.needsUpdate()

    if (show) {
        FloatingActionButton(
            onClick = { onRefresh() },
        ) {
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
            onGoToShoppingList = {},
            onGoToRecipeList = {},
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
        )
    RecipesAppTheme {
        RecipeDetails(recipe = recipe, modifier = Modifier.padding(4.dp))
    }
}

@PreviewLightDark
@Composable
fun RecipeDetailsBottomBarPreview() {
    val recipe =
        Recipe(
            id = 1,
            title = "Recipe 1",
            updatedAtLocally = LocalDateTime.now(),
            updatedAtRemotely = LocalDateTime.now(),
        )
    RecipesAppTheme {
        RecipeDetailsBottomBar(recipe = recipe)
    }
}
