package com.katharina.recipesapp.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.katharina.recipesapp.data.Recipe
import com.katharina.recipesapp.ui.theme.RecipesAppTheme

@Composable
fun RecipeList(
    recipes: List<Recipe>,
    onRecipeSelected: (Int) -> Unit,
) {
    if (recipes.isEmpty()) return RecipeEmptyList()

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
fun RecipeEmptyList() {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "No recipes found",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
        )

        Text(
            text = "\uD83E\uDD7A",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Composable
fun RecipeListItem(
    recipe: Recipe,
    onRecipeSelected: (Int) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (recipe.imageUrl == null) {
            Text(
                text = "\uD83C\uDF72",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary,
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
fun RecipeEmptyListPreview() {
    val recipes = emptyList<Recipe>()
    RecipesAppTheme {
        RecipeList(
            recipes = recipes,
            onRecipeSelected = {},
        )
    }
}
