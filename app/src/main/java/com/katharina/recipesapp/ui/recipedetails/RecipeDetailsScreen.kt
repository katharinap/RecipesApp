package com.katharina.recipesapp.ui.recipedetails

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.katharina.recipesapp.R
import com.katharina.recipesapp.data.Recipe
import com.katharina.recipesapp.ui.theme.RecipesAppTheme
import com.katharina.recipesapp.ui.utils.LoadingScreen
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

    val recipe =
        when (uiState) {
            is RecipeDetailsViewModel.UiState.Success -> {
                (uiState as RecipeDetailsViewModel.UiState.Success).recipe
            }

            else -> {
                null
            }
        }

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
        floatingActionButton = {
            RecipeDetailsRefreshButton(
                recipe = recipe,
                onRefresh = viewModel::updateRecipe,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->

        when (uiState) {
            is RecipeDetailsViewModel.UiState.Loading -> {
                LoadingScreen(Modifier.padding(innerPadding))
            }

            is RecipeDetailsViewModel.UiState.Success -> {
                RecipeDetails(
                    recipe = recipe!!,
                    onAddIngredientsToShoppingList = viewModel::addIngredientsToShoppingList,
                    Modifier.padding(innerPadding),
                )
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

        Card(
            modifier = Modifier.padding(4.dp).fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp).fillMaxWidth(),
            ) {
                Text(
                    text =
                        if (recipe.isGerman()) {
                            stringResource(R.string.ingredients_de)
                        } else {
                            stringResource(
                                R.string.ingredients_en,
                            )
                        },
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
                    Icon(
                        painter = painterResource(R.drawable.outline_list_alt_add_24),
                        contentDescription = "Add to Shopping List",
                    )
                }
            }
            recipe.ingredients.forEach { ingredient ->
                Text(
                    text = "• $ingredient",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(4.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        recipe.directions.forEach { direction ->
            Text(
                text = direction,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsTopBar(
    recipe: Recipe?,
    onToggleStarred: () -> Unit,
) {
    if (recipe == null) {
        return
    }

    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
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
                Text(
                    text = recipe.title,
                    modifier =
                        Modifier.padding(
                            start = 10.dp,
                        ),
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

@Composable
fun RecipeDetailsRefreshButton(
    recipe: Recipe?,
    onRefresh: () -> Unit = {},
) {
    if (recipe == null) {
        return
    }

    var isMenuOpen by remember { mutableStateOf(false) }

    if (isMenuOpen) {
        val sendIntent: Intent =
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, recipe.source)
                type = "text/plain"
            }
        val shareIntent = Intent.createChooser(sendIntent, null)

        val editIntent =
            Intent(
                Intent.ACTION_VIEW,
                recipe.getEditUrl().toUri(),
            )
        val context = LocalContext.current

        Column(
            horizontalAlignment = Alignment.End,
        ) {
            ExtendedFloatingActionButton(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Update Recipe",
                    )
                },
                text = { Text(text = "Update") },
                onClick = {
                    onRefresh()
                    isMenuOpen = false
                },
            )
            Spacer(modifier = Modifier.height(4.dp))
            ExtendedFloatingActionButton(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Recipe",
                    )
                },
                text = { Text(text = "Share") },
                onClick = {
                    context.startActivity(shareIntent)
                    isMenuOpen = false
                },
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (recipe.hasUrlSource()) {
                ExtendedFloatingActionButton(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Recipe",
                        )
                    },
                    text = { Text(text = "Edit") },
                    onClick = {
                        context.startActivity(editIntent)
                        isMenuOpen = false
                    },
                )
            }

            SmallFloatingActionButton(
                onClick = { isMenuOpen = !isMenuOpen },
                shape = CircleShape,
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close FAB menu",
                )
            }
        }
    } else {
        SmallFloatingActionButton(
            onClick = { isMenuOpen = !isMenuOpen },
            shape = CircleShape,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Open FAB menu",
            )
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
