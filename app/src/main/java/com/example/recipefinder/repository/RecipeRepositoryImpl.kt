package com.example.recipefinder.repository

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.recipefinder.model.FavoritesModel
import com.example.recipefinder.model.RecipeModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.InputStream
import java.util.concurrent.Executors

class RecipeRepositoryImpl:RecipeRepository {

    val database :FirebaseDatabase  = FirebaseDatabase.getInstance()
    val ref:DatabaseReference = database.reference.child("recipes")


    override fun addRecipe(recipeModel: RecipeModel, callBack: (Boolean, String) -> Unit) {
        var id = ref.push().key.toString()
        recipeModel.recipeId = id

        ref.child(id).setValue(recipeModel).addOnCompleteListener {
            if(it.isSuccessful){
                callBack(true,"Recipe Added Successfully")
            }else{
                callBack(false,"${it.exception?.message}")
            }
        }

    }

    override fun updateRecipe(
        recipeId: String,
        data: MutableMap<String, Any>,
        callBack: (Boolean, String) -> Unit
    ) {
        ref.child(recipeId).updateChildren(data).addOnCompleteListener {
            if(it.isSuccessful){
                callBack(true,"Recipe Updated Successfully")
            }else{
                callBack(false,"${it.exception?.message}")
            }
        }
    }

    override fun deleteRecipe(recipeId: String, callBack: (Boolean, String) -> Unit) {
        ref.child(recipeId).removeValue().addOnCompleteListener{

            if(it.isSuccessful){
                callBack(true,"Recipe Deleted Successfully")
            }else{
                callBack(false,"${it.exception?.message}")
            }
        }
    }

    override fun getRecipeById(
        recipeId: String,
        callBack: (RecipeModel?, Boolean, String) -> Unit
    ) {
        ref.child(recipeId).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               if(snapshot.exists()){
                   var model = snapshot.getValue(RecipeModel::class.java)
                   callBack(model, true, "Recipe fetched successfully")
               }
            }

            override fun onCancelled(error: DatabaseError) {
                callBack(null, false, error.message)
            }
        })
    }

    override fun getAllRecipes(callBack: (List<RecipeModel>?, Boolean, String) -> Unit) {
        ref.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var recipes = mutableListOf<RecipeModel>()

                if(snapshot.exists()){
                    for(eachRecipe in snapshot.children){
                        var data = eachRecipe.getValue(RecipeModel::class.java)
                        if(data!=null){
                            recipes.add(data)
                        }
                    }

                    callBack(recipes, true, "Recipes added successfully")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callBack(null, false, error.message)
            }


        } )
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

                var imageUrl = response["url"] as String?

                imageUrl = imageUrl?.replace("http://", "https://")

                Handler(Looper.getMainLooper()).post {
                    callback(imageUrl)
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

    //added code for favorites

    override fun isRecipeFavorite(recipeId: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun addToFavorites(
        favoritesModel: FavoritesModel,
        callBack: (Boolean, String) -> Unit
    ) {
        TODO("Not yet implemented")
    }
}