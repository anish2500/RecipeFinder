package com.example.recipefinder.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.recipefinder.R
import com.example.recipefinder.databinding.ActivityViewRecipeBinding
import com.example.recipefinder.repository.RecipeRepositoryImpl
import com.example.recipefinder.ui.fragment.FavoritesFragment
import com.example.recipefinder.viewModel.FavoritesViewModel
import com.example.recipefinder.viewModel.RecipeViewModel
import com.squareup.picasso.Picasso

class ViewRecipeActivity : AppCompatActivity() {
    lateinit var binding: ActivityViewRecipeBinding
    lateinit var recipeViewModel: RecipeViewModel
    lateinit var favoritesViewModel: FavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityViewRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        val repo = RecipeRepositoryImpl()
        recipeViewModel = RecipeViewModel(repo)
        favoritesViewModel = FavoritesViewModel(repo)

        // Get the recipeId from the Intent
        val recipeId = intent.getStringExtra("recipeId") ?: run {
            Toast.makeText(this, "Recipe ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Retrieve recipe details using the recipeId
        recipeViewModel.getRecipeById(recipeId).observe(this, Observer { recipe ->
            if (recipe != null) {
                // Update the UI with the recipe details
                binding.recipeName.setText(recipe.recipeName)
                binding.recipeDesc.setText(recipe.recipeDescription)
                binding.recipeIngredients.setText(recipe.recipeIngredients)
                binding.recipeSteps.setText(recipe.recipeSteps)

                // Load image using Picasso
                Picasso.get().load(recipe.imageUrl).into(binding.imageBrowse)
            } else {
                Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show()
            }
        })

        // Add a click listener to the "Add to Favorites" button


        // Safely apply window insets if the view exists
        findViewById<View>(R.id.main)?.let { mainView ->
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }
}
