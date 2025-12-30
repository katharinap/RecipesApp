package com.katharina.recipesapp.ui.utils

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.katharina.recipesapp.AppDestination
import com.katharina.recipesapp.R

@Composable
fun RecipeBottomAppBar(navController: NavHostController) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        NavigationBar(
            windowInsets = NavigationBarDefaults.windowInsets,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
        ) {
            NavigationBarItem(
                selected = currentDestination?.route == AppDestination.RecipeFavorites::class.qualifiedName,
                onClick = { navController.navigate(AppDestination.RecipeFavorites) },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_star_24),
                        contentDescription = "Favorites",
                    )
                },
                label = { Text("Favorites") },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.tertiary,
                        selectedTextColor = MaterialTheme.colorScheme.tertiary,
                        indicatorColor = MaterialTheme.colorScheme.tertiaryContainer,
                    ),
            )
            NavigationBarItem(
                selected = currentDestination?.route == AppDestination.RecipeList::class.qualifiedName,
                onClick = { navController.navigate(AppDestination.RecipeList) },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_cookie_24),
                        contentDescription = "Recipes",
                    )
                },
                label = { Text("Recipes") },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.tertiary,
                        selectedTextColor = MaterialTheme.colorScheme.tertiary,
                        indicatorColor = MaterialTheme.colorScheme.tertiaryContainer,
                    ),
            )

            NavigationBarItem(
                selected = currentDestination?.route == AppDestination.ShoppingList::class.qualifiedName,
                onClick = { navController.navigate(AppDestination.ShoppingList) },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_list_alt_24),
                        contentDescription = "Shopping List",
                    )
                },
                label = { Text("Shopping List") },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.tertiary,
                        selectedTextColor = MaterialTheme.colorScheme.tertiary,
                        indicatorColor = MaterialTheme.colorScheme.tertiaryContainer,
                    ),
            )
        }
    }
}
