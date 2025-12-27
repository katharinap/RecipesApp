package com.katharina.recipesapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.katharina.recipesapp.ui.login.LoginScreen
import com.katharina.recipesapp.ui.login.LoginViewModel
import com.katharina.recipesapp.ui.recipedetails.RecipeDetailsScreen
import com.katharina.recipesapp.ui.recipedetails.RecipeDetailsViewModel
import com.katharina.recipesapp.ui.recipelist.RecipeListScreen
import com.katharina.recipesapp.ui.recipelist.RecipeListViewModel
import com.katharina.recipesapp.ui.shoppinglist.ShoppingListScreen
import com.katharina.recipesapp.ui.shoppinglist.ShoppingListViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: AppDestination,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable<AppDestination.RecipeList> { navBackStackEntry ->
            RecipeListScreen(
                viewModel = hiltViewModel<RecipeListViewModel>(),
                onRecipeSelected = { recipeId ->
                    navController.navigate(AppDestination.RecipeDetails(recipeId))
                },
            )
        }

        composable<AppDestination.RecipeDetails> { navBackStackEntry ->
            RecipeDetailsScreen(viewModel = hiltViewModel<RecipeDetailsViewModel>())
        }

        composable<AppDestination.ShoppingList> { navBackStackEntry ->
            ShoppingListScreen(viewModel = hiltViewModel<ShoppingListViewModel>())
        }

        composable<AppDestination.Login> { navBackStackEntry ->
            LoginScreen(viewModel = hiltViewModel<LoginViewModel>())
        }
    }
}
