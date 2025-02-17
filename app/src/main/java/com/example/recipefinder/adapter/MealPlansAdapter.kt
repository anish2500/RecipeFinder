package com.example.recipefinder.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipefinder.R
import com.example.recipefinder.adapter.RecipeAdapter.ProductViewHolder

import com.example.recipefinder.model.MealPlansModel

import com.example.recipefinder.ui.activity.UpdateMealPlansActivity
import com.example.recipefinder.ui.activity.UpdateRecipeActivity
import java.util.ArrayList

class MealPlansAdapter(
 val context: Context,  // Passed explicitly
 var data: ArrayList<MealPlansModel>,  // List of MealPlanModel
 val onItemClick: (MealPlansModel) -> Unit,  // Click listener for item click
) : RecyclerView.Adapter<MealPlansAdapter.MealPlanViewHolder>() {

 class MealPlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  val mealType: TextView = itemView.findViewById(R.id.mealType)
  val mealName : TextView = itemView.findViewById(R.id.mealName)
  val edit: TextView = itemView.findViewById(R.id.updateMealPlan)
  val mealImage: ImageView = itemView.findViewById(R.id.mealImage)
 }

 override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealPlanViewHolder {
  val itemView: View = LayoutInflater.from(context)
   .inflate(R.layout.sample_mealplans, parent, false)
  return MealPlanViewHolder(itemView)
 }

 override fun getItemCount(): Int {
  return data.size
 }

 override fun onBindViewHolder(holder: MealPlanViewHolder, position: Int) {
  val mealplan = data [position]
  holder.mealType.text = data[position].mealType

  holder.mealName.text = data[position].mealName


  Glide.with(holder.itemView.context)
   .load(mealplan.mealimageUrl)
   .placeholder(R.drawable.imageplaceholder)
   .error(R.drawable.imageplaceholder)
   .into(holder.mealImage)

  holder.itemView.setOnClickListener {
   if (mealplan.mealPlanId.isNullOrEmpty()) {
    Toast.makeText(holder.itemView.context, "Error: Meals ID missing", Toast.LENGTH_SHORT).show()
   } else {
    onItemClick(mealplan) // Safe to call now
   }
  }



  holder.edit.setOnClickListener{//class bata activity ma jana
   val intent = Intent(context, UpdateMealPlansActivity::class.java)
   intent.putExtra("mealPlanId", data[position].mealPlanId)
   context.startActivity(intent)//context.startactivity le chai euta class bata arko activity ma jana milxa
  }
 }

 // Method for updating the list of meal plans
 fun updateData(newMealPlans: List<MealPlansModel>) {
  data.clear()
  data.addAll(newMealPlans)
  notifyDataSetChanged()
 }

 // Get Meal Plan ID for a specific position
 fun getMealPlanId(position: Int): String {
  return data[position].mealPlanId
 }
}
