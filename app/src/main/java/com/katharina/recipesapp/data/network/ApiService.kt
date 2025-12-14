package com.katharina.recipesapp.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiService {
    @GET(value = Constants.RECIPES_URL)
    suspend fun getRecipes(
        @Header("Authorization") accessToken: String,
    ): List<RecipeListResponseItem>

    @GET(Constants.RECIPE_DETAILS_URL)
    suspend fun getRecipe(
        @Path(value = "recipeId") recipeId: Int,
        @Header("Authorization") accessToken: String,
    ): RecipeDetailsResponse
}
