package com.example.recipefinder.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipefinder.model.RecipeModel
import com.example.recipefinder.model.UserModel
import com.example.recipefinder.repository.RecipeRepository
import com.example.recipefinder.repository.UserRepository

class RecipeViewModel(val repo: RecipeRepository)  {




    fun addRecipe(recipeModel: RecipeModel,
                  callback: (Boolean, String) -> Unit)
    {
        repo.addRecipe(recipeModel, callback)

    }





    fun updateRecipe(recipeId:String, data:MutableMap<String,Any>,
                      callback: (Boolean, String) -> Unit)
    {
        repo.updateRecipe(recipeId, data, callback)

    }




    fun deleteRecipe(productId: String,
                      callback: (Boolean, String) -> Unit){

        repo.deleteRecipe(productId, callback)
    }

    var _recipes  = MutableLiveData<RecipeModel>()
    var recipes = MutableLiveData<RecipeModel>()
        get() = _recipes

    var _allRecipes = MutableLiveData<List<RecipeModel>>()

    var allRecipes = MutableLiveData<List<RecipeModel>>()
        get() = _allRecipes


    fun getRecipeById(recipeId: String): LiveData<RecipeModel> {
        repo.getRecipeById(recipeId) { recipe, success, message ->
            if (success) {
                _recipes.value = recipe // Update the _recipes LiveData
            } else {
                _recipes.value = null // Handle the case where the recipe is not found
            }
        }
        return _recipes // Return the _recipes LiveData
    }


    var _loading = MutableLiveData<Boolean>()
    var loading = MutableLiveData<Boolean>()

    get() = _loading


    fun getAllRecipes(){
        _loading.value = true
        repo.getAllRecipes{
            recipes, success, message ->
            if(success){

                _allRecipes.value = recipes
                _loading.value = false
            }
        }
    }

    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit){
        repo.uploadImage(context, imageUri, callback)
    }
//added for search feature
    fun searchRecipes(query: String): List<RecipeModel> {
        return _allRecipes.value?.filter { recipe ->
            recipe.recipeName.contains(query, ignoreCase = true) ||
                    recipe.recipeDescription.contains(query, ignoreCase = true) ||
                    recipe.recipeIngredients.contains(query, ignoreCase = true)
        } ?: emptyList()
    }







}