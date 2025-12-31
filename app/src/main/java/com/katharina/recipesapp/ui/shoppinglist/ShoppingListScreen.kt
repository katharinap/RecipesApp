package com.katharina.recipesapp.ui.shoppinglist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.katharina.recipesapp.data.ShoppingListItem
import com.katharina.recipesapp.ui.theme.RecipesAppTheme
import com.katharina.recipesapp.ui.theme.handwritingFamily
import com.katharina.recipesapp.ui.utils.LoadingScreen
import com.katharina.recipesapp.ui.utils.RecipeBottomAppBar

@Composable
fun ShoppingListScreen(
    viewModel: ShoppingListViewModel,
    navController: NavHostController,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ShoppingListTopBar(
                nextItem = viewModel.nextItem,
                onUpdateNextItem = viewModel::updateNextItem,
                onAddItem = viewModel::addItem,
            )
        },
        bottomBar = {
            RecipeBottomAppBar(navController = navController)
        },
        floatingActionButton = { ShoppingListFab(onClick = viewModel::deleteAllChecked) },
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (uiState) {
            is ShoppingListViewModel.UiState.Loading -> {
                LoadingScreen(modifier = modifier)
            }

            is ShoppingListViewModel.UiState.Success -> {
                val items = (uiState as ShoppingListViewModel.UiState.Success).items
                ShoppingList(modifier = modifier, listItems = items, onItemClicked = viewModel::toggleCheckedState)
            }
        }
    }
}

@Composable
fun ShoppingListFab(onClick: () -> Unit) {
    Row {
        SmallFloatingActionButton(
            onClick = onClick,
            shape = CircleShape,
        ) {
            Icon(Icons.Default.Done, contentDescription = "Delete All")
        }
    }
}

@Composable
fun ShoppingList(
    modifier: Modifier,
    listItems: List<ShoppingListItem>,
    onItemClicked: (ShoppingListItem) -> Unit = {},
) {
    LazyColumn(
        modifier =
            modifier.then(
                Modifier
                    .padding(4.dp)
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background),
            ),
    ) {
        items(items = listItems) { item ->
            Text(
                text = item.name,
                fontFamily = handwritingFamily,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                modifier = Modifier.clickable { onItemClicked(item) }.padding(4.dp),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListTopBar(
    nextItem: String,
    onUpdateNextItem: (String) -> Unit,
    onAddItem: () -> Unit,
) {
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
                        query = nextItem,
                        onQueryChange = {
                            onUpdateNextItem(it)
                        },
                        onSearch = { onAddItem() },
                        expanded = false,
                        onExpandedChange = {},
                        leadingIcon = {
                            IconButton(onClick = { onUpdateNextItem("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = { onAddItem() }) {
                                Icon(
                                    imageVector = Icons.Default.Done,
                                    contentDescription = "Add",
                                )
                            }
                        },
                        placeholder = { Text(text = "Item") },
                    )
                },
                expanded = false,
                onExpandedChange = { },
            ) {}
            Spacer(modifier = Modifier.padding(4.dp))
        }
    }
}

@PreviewLightDark
@Composable
fun ShoppingListTopBarPreview() {
    RecipesAppTheme {
        ShoppingListTopBar(
            nextItem = "Item 1",
            onUpdateNextItem = {},
            onAddItem = {},
        )
    }
}

@PreviewLightDark
@Composable
fun ShoppingListPreview() {
    val itemList =
        listOf(
            ShoppingListItem(name = "Item 1"),
            ShoppingListItem(name = "Item 2", isChecked = true),
            ShoppingListItem(name = "Item 3"),
        )
    RecipesAppTheme {
        ShoppingList(
            modifier = Modifier,
            listItems = itemList,
        )
    }
}
