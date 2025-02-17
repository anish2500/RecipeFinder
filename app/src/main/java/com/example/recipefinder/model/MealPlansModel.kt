package com.example.recipefinder.model



import android.os.Parcel
import android.os.Parcelable

data class MealPlansModel(
    var mealPlanId: String = "",
    var mealType: String = "",
    var mealName: String = "",
    var mealProtein : String = "",
    var mealCalories : String = "",
    var mealimageUrl: String = "",
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ){}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mealPlanId)
        parcel.writeString(mealType)
        parcel.writeString(mealName)
        parcel.writeString(mealProtein)
        parcel.writeString(mealCalories)
        parcel.writeString(mealimageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MealPlansModel> {
        override fun createFromParcel(parcel: Parcel): MealPlansModel {
            return MealPlansModel(parcel)
        }

        override fun newArray(size: Int): Array<MealPlansModel?> {
            return arrayOfNulls(size)
        }
    }


}