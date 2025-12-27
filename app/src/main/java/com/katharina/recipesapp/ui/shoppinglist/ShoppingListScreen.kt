package com.katharina.recipesapp.ui.shoppinglist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.katharina.recipesapp.R
import com.katharina.recipesapp.data.ShoppingListItem
import com.katharina.recipesapp.ui.LoadingScreen
import com.katharina.recipesapp.ui.theme.RecipesAppTheme

@Composable
fun ShoppingListScreen(
    viewModel: ShoppingListViewModel,
    onGoToRecipeList: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { ShoppingListTopBar(onGoToRecipeList = onGoToRecipeList) },
        bottomBar = {
            ShoppingListBottomBar(
                nextItem = viewModel.nextItem,
                onUpdateNextItem = viewModel::updateNextItem,
                onAddItem = viewModel::addItem,
            )
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
fun ShoppingListBottomBar(
    nextItem: String,
    onUpdateNextItem: (String) -> Unit,
    onAddItem: () -> Unit,
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = nextItem,
            onValueChange = {
                onUpdateNextItem(it)
            },
            label = { Text("Item") },
            singleLine = true,
            leadingIcon = {
                IconButton(
                    onClick = {
                        onUpdateNextItem("")
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                    )
                }
            },
            trailingIcon = {
                FilledTonalButton(
                    onClick = {
                        onAddItem()
                        keyboardController?.hide()
                    },
                    shape = FloatingActionButtonDefaults.extendedFabShape,
                    modifier = Modifier.padding(horizontal = 10.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Add",
                    )
                }
            },
        )
    }
}

@Composable
fun ShoppingListFab(onClick: () -> Unit) {
    Row {
        FloatingActionButton(
            onClick = onClick,
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
    LazyColumn(modifier = modifier.then(Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background))) {
        items(items = listItems) { item ->
            Text(
                text = item.name,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                modifier = Modifier.clickable { onItemClicked(item) }.padding(4.dp),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListTopBar(onGoToRecipeList: () -> Unit) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row {
                    Icon(
                        painter = painterResource(R.drawable.baseline_list_alt_24),
                        contentDescription = "List",
                    )
                    Text(
                        "Shopping List",
                        modifier =
                            Modifier.padding(
                                start = 10.dp,
                            ),
                    )
                }
                Icon(
                    painter = painterResource(R.drawable.baseline_cookie_24),
                    contentDescription = "Cookie",
                    modifier =
                        Modifier.clickable { onGoToRecipeList() }.padding(end = 10.dp),
                )
            }
        },
        colors =
            topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
    )
}

@PreviewLightDark
@Composable
fun ShoppingListTopBarPreview() {
    RecipesAppTheme {
        ShoppingListTopBar(onGoToRecipeList = {})
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

@PreviewLightDark
@Composable
fun ShoppingListBottomBarPreview() {
    RecipesAppTheme {
        ShoppingListBottomBar(
            nextItem = "Item 1",
            onUpdateNextItem = {},
            onAddItem = {},
        )
    }
}
