package com.katharina.recipesapp.ui.recipelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.katharina.recipesapp.R
import com.katharina.recipesapp.ui.theme.RecipesAppTheme
import com.katharina.recipesapp.ui.utils.LoadingScreen
import com.katharina.recipesapp.ui.utils.RecipeBottomAppBar
import com.katharina.recipesapp.ui.utils.RecipeList
import com.katharina.recipesapp.ui.utils.TagItem

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
                taglist = viewModel.taglist,
                onTagSearch = { viewModel.searchRecipesByTag(it) },
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
    taglist: List<String> = emptyList(),
    onTagSearch: (String) -> Unit = {},
) {
    var showSearchField by remember { mutableStateOf(false) }
    var showTagSearchDialog by remember { mutableStateOf(false) }
    if (showTagSearchDialog) {
        TagSearchDialog(
            taglist = taglist,
            onSearch = onTagSearch,
            onDismissRequest = { showTagSearchDialog = false },
        )
    }

    if (showSearchField) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
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
                                IconButton(onClick = { showSearchField = false }) {
                                    Icon(Icons.Default.Search, contentDescription = "Search")
                                }
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        onSearch("")
                                        showSearchField = false
                                    },
                                ) {
                                    Icon(Icons.Default.Clear, "Clear")
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    },
                    expanded = false,
                    onExpandedChange = { },
                ) { }
                Spacer(modifier = Modifier.padding(4.dp))
            }
        }
    } else {
        TopAppBar(
            title = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    IconButton(onClick = { showTagSearchDialog = true }) {
                        Icon(
                            painter = painterResource(R.drawable.outline_tag_24),
                            contentDescription = "Tags",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }

                    IconButton(onClick = { showSearchField = true }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Tags",
                            tint = MaterialTheme.colorScheme.primary,
                        )
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
}

@Composable
fun TagSearchDialog(
    taglist: List<String> = emptyList(),
    onSearch: (String) -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            shape = RoundedCornerShape(16.dp),
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.outline_tag_24),
                        contentDescription = "Tags",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = "Tag Search",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            HorizontalDivider(thickness = 2.dp)
            FlowRow(modifier = Modifier.padding(8.dp)) {
                taglist.forEach { tag ->
                    TagItem(
                        tag = tag,
                        onClick = {
                            onSearch(tag)
                            onDismissRequest()
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeListRefreshButton(onClick: () -> Unit) {
    SmallFloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
    ) {
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

@PreviewLightDark
@Composable
fun TagSearchDialogPreview() {
    val taglist = (1..50).map { idx -> "tag$idx" }
    RecipesAppTheme {
        TagSearchDialog(taglist = taglist, onSearch = {}, onDismissRequest = {})
    }
}
