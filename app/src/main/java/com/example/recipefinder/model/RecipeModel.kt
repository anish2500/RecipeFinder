package com.example.recipefinder.model

import android.os.Parcel
import android.os.Parcelable

data class RecipeModel(
     var recipeId : String  = "",
     var recipeName: String  = "",
     var recipeDescription: String  = "",
     var recipeIngredients: String  = "",
     var recipeSteps: String  = "",
     var imageUrl : String = "",
     var isFavorites: Boolean = false,

 ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",

    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(recipeId)
        parcel.writeString(recipeName)
        parcel.writeString(recipeDescription)
        parcel.writeString(recipeIngredients)
        parcel.writeString(recipeSteps)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecipeModel> {
        override fun createFromParcel(parcel: Parcel): RecipeModel {
            return RecipeModel(parcel)
        }

        override fun newArray(size: Int): Array<RecipeModel?> {
            return arrayOfNulls(size)
        }
    }
}