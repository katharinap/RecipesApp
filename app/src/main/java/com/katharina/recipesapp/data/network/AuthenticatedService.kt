package com.katharina.recipesapp.data.network

import retrofit2.http.GET
import retrofit2.http.Header

interface AuthenticatedService {
    @GET(Constants.RECIPE_DETAILS_URL)
    suspend fun getRecipe(
        @Header("Authorization") accessToken: String,
    ): RecipeDetailsResponse
}
