package com.example.recipefinder.repository

import com.example.recipefinder.model.RecipeModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FavoritesRepositoryImpl : FavoritesRepository {

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val ref: DatabaseReference = database.reference.child("favorites")

    override fun addToFavorites(recipe: RecipeModel, callback: (Boolean, String) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun removeFavorites(recipeId: String) {
        TODO("Not yet implemented")
    }

    override fun getAllFavorites(): List<RecipeModel> {
        TODO("Not yet implemented")
    }

}