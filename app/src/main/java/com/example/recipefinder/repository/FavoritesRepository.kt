package com.example.recipefinder.repository

import com.example.recipefinder.model.RecipeModel


interface FavoritesRepository {

    fun addToFavorites(recipe: RecipeModel, callback: (Boolean, String) -> Unit)
    fun removeFavorites(recipeId: String)
    fun getAllFavorites(): List<RecipeModel>

}
