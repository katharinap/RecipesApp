package com.katharina.recipesapp.ui.recipelist

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
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
        topBar = {
            RecipeListTopBar(
                taglist = viewModel.taglist,
                onTagSelected = viewModel::loadRecipesWithTag,
            )
        },
        bottomBar = {
            RecipeListBottomBar(
                query = viewModel.query,
                onSearch = { viewModel.searchRecipes(it) },
            )
        },
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
fun RecipeListTopBar(
    taglist: List<String>,
    onTagSelected: (String) -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        colors =
            topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
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

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                ) {
                    taglist.forEach { tag ->
                        DropdownMenuItem(
                            text = { Text(tag) },
                            onClick = {
                                menuExpanded = false
                                onTagSelected(tag)
                            },
                        )
                    }
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = { menuExpanded = !menuExpanded }) {
                Icon(Icons.Default.Menu, "Tags", tint = MaterialTheme.colorScheme.primary)
            }
        },
    )
}

@Composable
fun RecipeListBottomBar(
    query: String,
//    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = query,
            onValueChange = { onSearch(it) },
            label = { Text("Filter") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Filter") },
            trailingIcon = {
                FilledTonalIconButton(onClick = { onSearch("") }) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Clear",
                    )
                }
            },
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
    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items = recipes) { recipe ->
            RecipeListItem(recipe = recipe, onRecipeSelected = onRecipeSelected)
        }
    }
}

@Composable
fun RecipeListItem(
    recipe: Recipe,
    onRecipeSelected: (Int) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (recipe.imageUrl == null) {
            Image(
                painter = painterResource(R.drawable.recipe_default_350),
                contentDescription = "Default Recipe Image",
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(16.dp)),
                alignment = Alignment.Center,
            )
        } else {
            recipe.getRemoteImageUrl()?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Recipe Image",
                    contentScale = ContentScale.Crop,
                    modifier =
                        Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(16.dp)),
                    alignment = Alignment.Center,
                )
            }
        }
        Text(
            text = recipe.title,
            modifier =
                Modifier
                    .padding(4.dp)
                    .clickable { onRecipeSelected(recipe.id) },
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

@PreviewLightDark
@Composable
fun RecipeListTopBarPreview() {
    RecipesAppTheme {
        RecipeListTopBar(
            taglist = listOf("tag1", "tag2", "tag3"),
            onTagSelected = {},
        )
    }
}

@PreviewLightDark
@Composable
fun RecipeListPreview() {
    val recipes =
        listOf(
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
        RecipeListBottomBar(
            query = "lentil",
            onSearch = {},
        )
    }
}
