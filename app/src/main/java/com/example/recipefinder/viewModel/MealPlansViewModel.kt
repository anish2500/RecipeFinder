package com.example.recipefinder.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipefinder.model.MealPlansModel
import com.example.recipefinder.model.RecipeModel
import com.example.recipefinder.repository.MealPlansRepository

class MealPlansViewModel(val repo: MealPlansRepository) {

    // Function to add a meal plan
    fun addMealPlan(mealPlanModel: MealPlansModel, callback: (Boolean, String) -> Unit) {
        repo.addMealPlan(mealPlanModel, callback)
    }

    // Function to update a meal plan
    fun updateMealPlan(mealPlanId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        repo.updateMealPlan(mealPlanId, data, callback)
    }

    // Function to delete a meal plan
    fun deleteMealPlan(mealPlanId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteMealPlan(mealPlanId, callback)
    }

    // LiveData to hold the current meal plan
    private var _mealPlans  = MutableLiveData<MealPlansModel>()
    var mealPlans = MutableLiveData<MealPlansModel>()
        get() = _mealPlans

    // LiveData to hold all meal plans
    private var _allMealPlans = MutableLiveData<List<MealPlansModel>>()
    var allMealPlans: LiveData<List<MealPlansModel>> = _allMealPlans

    // LiveData for loading state
    private var _loading = MutableLiveData<Boolean>()
    var loading: LiveData<Boolean> = _loading

    // Function to get meal plan by its ID
    fun getMealPlanById(mealPlanId: String): LiveData<MealPlansModel> {
        repo.getMealPlanById(mealPlanId) { mealPlan, success, message ->
            if (success) {
                _mealPlans.value = mealPlan
            } else {
                _mealPlans.value = null // Handle the case where the meal plan is not found
            }
        }
        return _mealPlans // Return the LiveData for the specific meal plan
    }

    // Function to fetch all meal plans
    fun getAllMealPlans() {
        _loading.value = true
        repo.getAllMealPlans { mealPlans, success, message ->
            if (success) {
                _allMealPlans.value = mealPlans
            }
            _loading.value = false
        }
    }

    // Function to upload an image to Cloudinary
    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        repo.uploadImage(context, imageUri, callback)
    }

    // Function to search meal plans by name, description, or ingredients
    fun searchMealPlans(query: String): List<MealPlansModel> {
        return _allMealPlans.value?.filter { mealPlan ->
            mealPlan.mealName.contains(query, ignoreCase = true) ||
                    mealPlan.mealType.contains(query, ignoreCase = true) ||
                    mealPlan.mealProtein.contains(query, ignoreCase = true)
        } ?: emptyList()
    }
}
