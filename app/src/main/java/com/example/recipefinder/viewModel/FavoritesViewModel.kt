package com.example.recipefinder.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipefinder.model.FavoritesModel
import com.example.recipefinder.model.RecipeModel
import com.example.recipefinder.repository.FavoritesRepository
import com.example.recipefinder.repository.FavoritesRepositoryImpl
import com.example.recipefinder.repository.RecipeRepositoryImpl

class FavoritesViewModel(private val repository: RecipeRepositoryImpl) : ViewModel() {

//    suspend fun addToFavorites(recipeId: String): Boolean {
//        return try {
//            val isFavorite = repository.isRecipeFavorite(recipeId) // Check if already a favorite
//            if (!isFavorite) {  // Only add if not already a favorite
//                repository.addToFavorites(FavoritesModel(recipeId = recipeId))
//                true
//            } else {
//                false // Already a favorite
//            }
//        } catch (e: Exception) {
//            false
//        }
//    }

    // ... other functions to get favorites, remove from favorites, etc.
}
