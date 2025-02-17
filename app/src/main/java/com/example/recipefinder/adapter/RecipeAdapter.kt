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
import com.example.recipefinder.model.RecipeModel
import com.example.recipefinder.ui.activity.UpdateRecipeActivity
import java.util.ArrayList

class RecipeAdapter(
    val context: Context,  // Passed explicitly
    var data: ArrayList<RecipeModel>,  // List of RecipeModel
    val onItemClick: (RecipeModel) -> Unit,  // Click listener for item click

) : RecyclerView.Adapter<RecipeAdapter.ProductViewHolder>(){

    class ProductViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){
        val pName : TextView = itemView.findViewById(R.id.displayName)

        val pDesc : TextView = itemView.findViewById(R.id.displayDesc)
        val edit : TextView = itemView.findViewById(R.id.lblEdit)
        val pImage: ImageView = itemView.findViewById(R.id.recipeImage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView : View = LayoutInflater.from(context).
        inflate(R.layout.sample_recipes,parent,false)

        return ProductViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val recipe = data [position]
        holder.pName.text = data[position].recipeName

        holder.pDesc.text = data[position].recipeDescription

        Glide.with(holder.itemView.context)
            .load(recipe.imageUrl)
            .placeholder(R.drawable.imageplaceholder)
            .error(R.drawable.imageplaceholder)
            .into(holder.pImage)

        holder.itemView.setOnClickListener {
            if (recipe.recipeId.isNullOrEmpty()) {
                Toast.makeText(holder.itemView.context, "Error: Recipe ID missing", Toast.LENGTH_SHORT).show()
            } else {
                onItemClick(recipe) // Safe to call now
            }
        }



        holder.edit.setOnClickListener{//class bata activity ma jana
            val intent = Intent(context, UpdateRecipeActivity::class.java)
            intent.putExtra("recipeId", data[position].recipeId)
            context.startActivity(intent)//context.startactivity le chai euta class bata arko activity ma jana milxa
        }
    }

    //recently modified code for search feature
    fun updateData(newRecipes: List<RecipeModel>){
        data.clear()
        data.addAll(newRecipes)
        notifyDataSetChanged()
    }

    fun getRecipeId(position:Int):String{
        return data[position].recipeId
    }

}