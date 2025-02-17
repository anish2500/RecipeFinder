package com.example.recipefinder.ui.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.anew.utils.LoadingUtils
import com.example.recipefinder.R
import com.example.recipefinder.databinding.ActivityAddRecipeBinding
import com.example.recipefinder.model.RecipeModel
import com.example.recipefinder.repository.RecipeRepository
import com.example.recipefinder.repository.RecipeRepositoryImpl
import com.example.recipefinder.utils.ImageUtils
import com.example.recipefinder.viewModel.RecipeViewModel
import com.squareup.picasso.Picasso

class AddRecipeActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddRecipeBinding
    lateinit var recipeViewModel: RecipeViewModel
    lateinit var loadingUtils: LoadingUtils
    lateinit var imageUtils: ImageUtils

    var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUtils = LoadingUtils(this)
        imageUtils = ImageUtils(this)

        var repo = RecipeRepositoryImpl()
        recipeViewModel = RecipeViewModel(repo)


        imageUtils.registerActivity { url->
            url.let{it ->
                imageUri = it
                Picasso.get().load(it).into(binding.imageBrowse)

            }
        }


        binding.imageBrowse.setOnClickListener{
            imageUtils.launchGallery(this)
        }





        binding.addBtn.setOnClickListener{

            uploadImage()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }



    private fun uploadImage() {
        loadingUtils.show()
        imageUri?.let { uri ->
            recipeViewModel.uploadImage(this, uri) { imageUrl ->
                Log.d("checpoirs", imageUrl.toString())
                if (imageUrl != null) {
                    addRecipe(imageUrl)
                } else {
                    Log.e("Upload Error", "Failed to upload image to Cloudinary")
                }
            }
        }
    }

    private fun addRecipe(url: String) {
        var productName = binding.recipeName.text.toString()

        var productDesc = binding.recipeDesc.text.toString()
        var recipeIngredients = binding.recipeIngredients.text.toString()
        var recipeSteps = binding.recipeSteps.text.toString()

        var model = RecipeModel(
            "",
            productName,
            productDesc,
            recipeIngredients,
            recipeSteps,
            url
        )

        recipeViewModel.addRecipe(model) { success, message ->
            if (success) {
                Toast.makeText(
                    this@AddRecipeActivity,
                    message, Toast.LENGTH_LONG
                ).show()
                finish()
                loadingUtils.dismiss()
            } else {
                Toast.makeText(
                    this@AddRecipeActivity,
                    message, Toast.LENGTH_LONG
                ).show()
                loadingUtils.dismiss()
            }
        }
    }
}