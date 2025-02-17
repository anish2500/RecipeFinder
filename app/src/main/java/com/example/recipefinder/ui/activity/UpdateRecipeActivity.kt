package com.example.recipefinder.ui.activity

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.anew.utils.LoadingUtils
import com.example.recipefinder.R
import com.example.recipefinder.databinding.ActivityUpdateRecipeBinding
import com.example.recipefinder.repository.RecipeRepositoryImpl
import com.example.recipefinder.utils.ImageUtils
import com.example.recipefinder.viewModel.RecipeViewModel
import com.squareup.picasso.Picasso

class UpdateRecipeActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateRecipeBinding
    lateinit var recipeViewModel: RecipeViewModel
    lateinit var loadingUtils: LoadingUtils
    lateinit var imageUtils: ImageUtils

    var imageUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUtils = LoadingUtils(this)
        imageUtils = ImageUtils(this)

        val repo = RecipeRepositoryImpl()
        recipeViewModel = RecipeViewModel(repo)

        val recipeId: String = intent.getStringExtra("recipeId").toString()

        recipeViewModel.getRecipeById(recipeId)

        recipeViewModel.recipes.observe(this) { recipe ->
            if (binding.updateRecipeName.text.isNullOrEmpty()) {
                binding.updateRecipeName.setText(recipe?.recipeName)
            }
            if (binding.updateRecipeDesc.text.isNullOrEmpty()) {
                binding.updateRecipeDesc.setText(recipe?.recipeDescription)
            }
            if (binding.updateRecipeIngredients.text.isNullOrEmpty()) {
                binding.updateRecipeIngredients.setText(recipe?.recipeIngredients)
            }
            if (binding.updateRecipeSteps.text.isNullOrEmpty()) {
                binding.updateRecipeSteps.setText(recipe?.recipeSteps)
            }

            val imageUrl = recipe?.imageUrl

            if (imageUrl.isNullOrEmpty()) {
                // Handle case if the imageUrl is null or empty (use a placeholder or skip loading)
                Glide.with(this)
                    .load(R.drawable.imageplaceholder)  // Use a default image placeholder
                    .into(binding.updateImage)
            } else {
                // Load the actual image from the URL
                Glide.with(this)
                    .load(imageUrl)
                    .into(binding.updateImage)
            }
        }

        binding.updateImage.setOnClickListener {
            imageUtils.launchGallery(this)
        }

        imageUtils.registerActivity { url ->
            url?.let {
                imageUri = it.toString()
                Picasso.get().load(it).into(binding.updateImage)
            }
        }

        binding.updateBtn.setOnClickListener {
            val name = binding.updateRecipeName.text.toString()
            val desc = binding.updateRecipeDesc.text.toString()
            val ingredients = binding.updateRecipeIngredients.text.toString()
            val steps = binding.updateRecipeSteps.text.toString()

            val updatedMap = mutableMapOf<String, Any>()

            updatedMap["recipeName"] = name
            updatedMap["recipeDescription"] = desc
            updatedMap["recipeIngredients"] = ingredients
            updatedMap["recipeSteps"] = steps

            if (imageUri != null) {
                uploadImageAndUpdateRecipe(recipeId, updatedMap)
            } else {
                // Proceed without updating image
                updateRecipe(recipeId, updatedMap)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // This function is responsible for uploading the image and then updating the recipe.
    private fun uploadImageAndUpdateRecipe(recipeId: String, updatedMap: MutableMap<String, Any>) {
        loadingUtils.show()
        imageUri?.let { uriString ->
            val uri = Uri.parse(uriString)
            recipeViewModel.uploadImage(this, uri) { uploadedImageUrl ->
                if (uploadedImageUrl != null) {
                    updatedMap["imageUrl"] = uploadedImageUrl
                    updateRecipe(recipeId, updatedMap)
                } else {
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                    loadingUtils.dismiss()
                }
            }
        }
    }

    // This function handles updating the recipe with the provided map of updated fields.
    private fun updateRecipe(recipeId: String, updatedMap: MutableMap<String, Any>) {
        recipeViewModel.updateRecipe(recipeId, updatedMap) { success, message ->
            if (success) {
                Toast.makeText(this@UpdateRecipeActivity, message, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@UpdateRecipeActivity, message, Toast.LENGTH_SHORT).show()
            }
            loadingUtils.dismiss()
        }
    }
}



