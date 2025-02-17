package com.example.recipefinder.repository

import android.content.Context
import android.net.Uri
import com.example.recipefinder.model.FavoritesModel
import com.example.recipefinder.model.RecipeModel

interface RecipeRepository {

    fun addRecipe(recipeModel: RecipeModel,
                  callBack: (Boolean, String) -> Unit)

    fun updateRecipe(recipeId: String, data:MutableMap<String, Any>,
                     callBack: (Boolean, String) -> Unit)

    fun deleteRecipe(recipeId: String,
                     callBack: (Boolean, String) -> Unit)
    fun getRecipeById(recipeId: String,
                      callBack: (RecipeModel?,Boolean, String) -> Unit)

    fun getAllRecipes(callBack: (List<RecipeModel>?,Boolean, String) -> Unit)


    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit)

    fun getFileNameFromUri(context: Context, uri: Uri): String?

    fun isRecipeFavorite(recipeId: String): Boolean
    fun addToFavorites(favoritesModel: FavoritesModel,
                       callBack: (Boolean, String) -> Unit)


}