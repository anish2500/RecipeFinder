package com.example.recipefinder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.R
import com.example.recipefinder.model.FavoritesModel

class FavoritesAdapter(
    private val context: Context,
    private var favoritesList: List<FavoritesModel>,
    private val onItemLongClick: (FavoritesModel) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    inner class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeName: TextView = itemView.findViewById(R.id.recipeName)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_favorites, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val favorite = favoritesList[position]
        holder.recipeName.text = favorite.recipeId // Replace with actual recipe name if available
    }

    override fun getItemCount(): Int {
        return favoritesList.size
    }

    fun updateData(newFavoritesList: List<FavoritesModel>) {
        favoritesList = newFavoritesList
        notifyDataSetChanged()
    }
}