package com.example.recipefinder.repository

import android.content.Context
import android.net.Uri
import com.example.recipefinder.model.MealPlansModel

interface MealPlansRepository {

    fun addMealPlan(mealPlanModel: MealPlansModel,
                    callBack: (Boolean, String) -> Unit)

    fun updateMealPlan(mealPlanId: String, data: MutableMap<String, Any>,
                       callBack: (Boolean, String) -> Unit)

    fun deleteMealPlan(mealPlanId: String,
                       callBack: (Boolean, String) -> Unit)

    fun getMealPlanById(mealPlanId: String,
                        callBack: (MealPlansModel?, Boolean, String) -> Unit)

    fun getAllMealPlans(callBack: (List<MealPlansModel>?, Boolean, String) -> Unit)

    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit)

    fun getFileNameFromUri(context: Context, uri: Uri): String?
}