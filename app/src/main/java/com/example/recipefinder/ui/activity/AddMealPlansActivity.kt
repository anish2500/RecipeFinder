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
import com.example.recipefinder.databinding.ActivityAddMealPlansBinding
import com.example.recipefinder.model.MealPlansModel
import com.example.recipefinder.repository.MealPlansRepositoryImpl
import com.example.recipefinder.utils.ImageUtils
import com.example.recipefinder.viewModel.MealPlansViewModel
import com.squareup.picasso.Picasso

class AddMealPlansActivity : AppCompatActivity() {
     lateinit var binding: ActivityAddMealPlansBinding
     lateinit var mealPlansViewModel: MealPlansViewModel
     lateinit var loadingUtils: LoadingUtils
     lateinit var imageUtils: ImageUtils

    var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddMealPlansBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize loadingUtils and imageUtils
        loadingUtils = LoadingUtils(this)
        imageUtils = ImageUtils(this)

        // Initialize ViewModel
        val repo = MealPlansRepositoryImpl()
        mealPlansViewModel = MealPlansViewModel(repo)

        // Set up image selection
        imageUtils.registerActivity { url ->
            url?.let {
                imageUri = it
                Picasso.get().load(it).into(binding.imageBrowse)
            }
        }

        // Set up click listeners
        binding.imageBrowse.setOnClickListener {
            imageUtils.launchGallery(this)
        }

        binding.addMealBtn.setOnClickListener {
            uploadImage()
        }

        // Move this after setContentView to ensure binding is initialized
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun uploadImage() {
        loadingUtils.show()
        imageUri?.let { uri ->
            mealPlansViewModel.uploadImage(this, uri) { imageUrl ->
                Log.d("Upload Check", imageUrl.toString())
                if (imageUrl != null) {
                    addMealPlan(imageUrl)
                } else {
                    Log.e("Upload Error", "Failed to upload image")
                    loadingUtils.dismiss()
                }
            }
        }
    }

    private fun addMealPlan(url: String) {
        val mealType = binding.mealType.text.toString()
        val mealName = binding.mealName.text.toString()
        val mealProtein = binding.mealProtein.text.toString()
        val mealCalories = binding.mealCalories.text.toString()

        val model = MealPlansModel(
            "",
            mealType,
            mealName,
            mealProtein,
            mealCalories,
            url
        )

        mealPlansViewModel.addMealPlan(model) { success, message ->
            if (success) {
                Toast.makeText(
                    this@AddMealPlansActivity,
                    message, Toast.LENGTH_LONG
                ).show()
                finish()
                loadingUtils.dismiss()
            } else {
                Toast.makeText(
                    this@AddMealPlansActivity,
                    message, Toast.LENGTH_LONG
                ).show()
                loadingUtils.dismiss()
            }
        }
    }
}