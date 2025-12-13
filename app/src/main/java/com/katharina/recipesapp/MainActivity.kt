package com.katharina.recipesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.katharina.recipesapp.ui.theme.RecipesAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
//    private val viewModelList: RecipeListViewModel by viewModels()
//    private val viewModelDetail: RecipeDetailsViewModel by viewModels()
//    private val viewModelLogin: LoginViewModel by viewModels()
//
//    private val screenName = "Login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipesAppTheme {
                AppNavHost(
                    startDestination = AppDestination.RecipeList,
                )
//                if (screenName == "List") {
//                    RecipeListScreen(viewModelList, onRecipeSelected = {})
//                } else if (screenName == "Details") {
//                    RecipeDetailsScreen(viewModelDetail)
//                } else if (screenName == "Login") {
//                    LoginScreenDummy(viewModel = viewModelLogin)
//                } else {
//                    Text("Error")
//                }
            }
        }
    }
}
