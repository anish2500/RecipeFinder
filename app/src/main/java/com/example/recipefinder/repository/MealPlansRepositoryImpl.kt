package com.example.recipefinder.repository

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.recipefinder.model.MealPlansModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.InputStream
import java.util.concurrent.Executors

class MealPlansRepositoryImpl : MealPlansRepository {

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val ref: DatabaseReference = database.reference.child("mealPlans")

    override fun addMealPlan(mealPlanModel: MealPlansModel, callBack: (Boolean, String) -> Unit) {
        var id = ref.push().key.toString()
        mealPlanModel.mealPlanId = id
        ref.child(id).setValue(mealPlanModel).addOnCompleteListener {
            if (it.isSuccessful) {
                callBack(true, "Meal Plans Added Successfully")
            } else {
                callBack(false, "${it.exception?.message}")
            }
        }
    }

    override fun updateMealPlan(
        mealPlanId: String,
        data: MutableMap<String, Any>,
        callBack: (Boolean, String) -> Unit
    ) {
        ref.child(mealPlanId).setValue(data).addOnCompleteListener {
            if (it.isSuccessful) {
                callBack(true, "Meal Plan Updated Successfully")
            } else {
                callBack(false, "${it.exception?.message}")
            }
        }
    }

    override fun deleteMealPlan(mealPlanId: String, callBack: (Boolean, String) -> Unit) {
        ref.child(mealPlanId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callBack(true, "Meal Plan Deleted Successfully")
            } else {
                callBack(false, "${it.exception?.message}")
            }
        }
    }

    override fun getMealPlanById(
        mealPlanId: String,
        callBack: (MealPlansModel?, Boolean, String) -> Unit
    ) {
        ref.child(mealPlanId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val model = snapshot.getValue(MealPlansModel::class.java)
                    if (model != null) {
                        callBack(model, true, "Meal Plan fetched successfully")
                    } else {
                        callBack(null, false, "Meal Plan data is null")
                    }
                } else {
                    callBack(null, false, "No data found for the meal plan")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callBack(null, false, error.message)
            }
        })
    }

    override fun getAllMealPlans(callBack: (List<MealPlansModel>?, Boolean, String) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val mealPlans = mutableListOf<MealPlansModel>()
                if (snapshot.exists()) {
                    for (eachMealPlan in snapshot.children) {
                        val data = eachMealPlan.getValue(MealPlansModel::class.java)
                        if (data != null) {
                            mealPlans.add(data)
                        }
                    }
                    callBack(mealPlans, true, "Meal Plans fetched successfully")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callBack(null, false, error.message)
            }
        })
    }

    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "dyx23ipck",
            "api_key" to "419674665319974",
            "api_secret" to "PIj_1iNB2JsB2YVp8y5VvlhBBR4"
        )
    )

    override fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
                var fileName = getFileNameFromUri(context, imageUri)
                fileName = fileName?.substringBeforeLast(".") ?: "uploaded_image"
                val response = cloudinary.uploader().upload(
                    inputStream, ObjectUtils.asMap(
                        "public_id", fileName,
                        "resource_type", "image"
                    )
                )
                var mealImageUrl = response["url"] as String?
                mealImageUrl = mealImageUrl?.replace("http.//", "https://")
                Handler(Looper.getMainLooper()).post {
                    callback(mealImageUrl)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post {
                    callback(null)
                }
            }
        }
    }

    override fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }
}
