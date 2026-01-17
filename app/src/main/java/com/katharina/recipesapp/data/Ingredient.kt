package com.katharina.recipesapp.data

class Ingredient {
    companion object {
        fun isHeading(ingredient: String) = ingredient.trim().endsWith(":")
    }
}
