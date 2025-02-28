package com.example.recipefinder.ui.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.anew.utils.LoadingUtils
import com.example.recipefinder.R
import com.example.recipefinder.databinding.ActivityUpdateMealPlansBinding
import com.example.recipefinder.repository.MealPlansRepositoryImpl
import com.example.recipefinder.utils.ImageUtils
import com.example.recipefinder.viewModel.MealPlansViewModel

class UpdateMealPlansActivity : AppCompatActivity() {

    lateinit var binding: ActivityUpdateMealPlansBinding
    lateinit var mealPlansViewModel: MealPlansViewModel
    lateinit var loadingUtils: LoadingUtils
    lateinit var imageUtils: ImageUtils

    var imageUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateMealPlansBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUtils = LoadingUtils(this)
        imageUtils = ImageUtils(this)

        // Initialize ViewModel using ViewModelProvider
        val repo = MealPlansRepositoryImpl()
        mealPlansViewModel = MealPlansViewModel(repo)

        val mealPlanId: String? = intent.getStringExtra("mealPlanId")

        Log.d("checkkkkkk", mealPlanId ?: "null")
        // Check if the mealId is valid
        if (mealPlanId.isNullOrEmpty()) {
            Toast.makeText(this, "Invalid Meal ID", Toast.LENGTH_SHORT).show()
            return
        }

        // Fetch the meal plan by ID
        mealPlansViewModel.getMealPlanById(mealPlanId).observe(this) { meal ->
            if (meal != null) {
                // Set input fields with the fetched data
                binding.updateMealType.setText(meal.mealType ?: "")
                binding.updateMealName.setText(meal.mealName ?: "")
                binding.updateMealProtein.setText(meal.mealProtein ?: "")
                binding.updateMealCalories.setText(meal.mealCalories ?: "")

                // Load image using Glide
                val imageUrl = meal.mealimageUrl.takeIf { it.isNotEmpty() }?.let { convertToHttps(it) }

                if (imageUrl.isNullOrEmpty()) {
                    // Handle case if the imageUrl is null or empty (use a placeholder or skip loading)
                    Glide.with(this)
                        .load(R.drawable.imageplaceholder)  // Use a default image placeholder
                        .into(binding.updateMealImage)
                } else {
                    // Load the actual image from the URL
                    Glide.with(this)
                        .load(imageUrl)
                        .into(binding.updateMealImage)
                }
            }
        }

        // Handle image selection from gallery
        binding.updateMealImage.setOnClickListener {
            imageUtils.launchGallery(this)
        }

        imageUtils.registerActivity { uri ->
            uri?.let {
                imageUri = it.toString()
                Glide.with(this)
                    .load(it)
                    .into(binding.updateMealImage)
            }
        }

        // Handle the update button click
        binding.updateMealBtn.setOnClickListener {
            val mealType = binding.updateMealType.text.toString()
            val mealName = binding.updateMealName.text.toString()
            val mealProtein = binding.updateMealProtein.text.toString()
            val mealCalories = binding.updateMealCalories.text.toString()

            val updatedMap = mutableMapOf<String, Any>()
            updatedMap["mealType"] = mealType
            updatedMap["mealName"] = mealName
            updatedMap["mealProtein"] = mealProtein
            updatedMap["mealCalories"] = mealCalories

            if (imageUri != null) {
                uploadImageAndUpdateMealPlan(mealPlanId, updatedMap)
            } else {
                updateMealPlan(mealPlanId, updatedMap)
            }
        }

        // Set window insets for edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun uploadImageAndUpdateMealPlan(mealId: String, updatedMap: MutableMap<String, Any>) {
        loadingUtils.show()
        imageUri?.let { uriString ->
            val uri = Uri.parse(uriString)
            mealPlansViewModel.uploadImage(this, uri) { uploadedImageUrl ->
                if (uploadedImageUrl != null) {
                    updatedMap["mealImageUrl"] = uploadedImageUrl
                    updateMealPlan(mealId, updatedMap)
                } else {
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                    loadingUtils.dismiss()
                }
            }
        }
    }

    private fun updateMealPlan(mealId: String, updatedMap: MutableMap<String, Any>) {
        loadingUtils.show()
        mealPlansViewModel.updateMealPlan(mealId, updatedMap) { success, message ->
            Toast.makeText(this@UpdateMealPlansActivity, message, Toast.LENGTH_SHORT).show()
            if (success) {
                finish()
            }
            loadingUtils.dismiss()
        }
    }

    private fun convertToHttps(url: String): String {
        return url.replace("http://", "https://")
    }
}