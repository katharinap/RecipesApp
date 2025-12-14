package com.katharina.recipesapp.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultRecipesRepository
    @Inject
    constructor() : RecipeRepository {
        val recipes =
            listOf(
                Recipe(
                    id = 1,
                    title = "Recipe 1",
                    directions = listOf("Description 1"),
                    ingredients = listOf("Ingredient 1", "Other Ingredient 1"),
                ),
                Recipe(
                    id = 2,
                    title = "Recipe 2",
                    directions = listOf("Description 2"),
                    ingredients = listOf("Ingredient 2", "Other Ingredient 2"),
                ),
                Recipe(
                    id = 3,
                    title = "Recipe 3",
                    directions = listOf("Description 3"),
                    ingredients = listOf("Ingredient 3", "Other Ingredient 3"),
                ),
                Recipe(
                    id = 4,
                    title = "Recipe 4",
                    directions = listOf("Description 4"),
                    ingredients = listOf("Ingredient 4", "Other Ingredient 4"),
                ),
            )

        override fun getAllRecipesStream(): Flow<List<Recipe>> = flowOf(recipes)

        override fun getRecipeById(id: Int): Flow<Recipe?> {
            val result = flowOf(recipes.first { it.id == id })
            return result
        }
    }
