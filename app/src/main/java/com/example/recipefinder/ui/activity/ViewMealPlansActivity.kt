package com.example.recipefinder.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.recipefinder.R
import com.example.recipefinder.databinding.ActivityViewMealPlansBinding

import com.example.recipefinder.repository.MealPlansRepositoryImpl

import com.example.recipefinder.viewModel.MealPlansViewModel
import com.squareup.picasso.Picasso

class ViewMealPlansActivity : AppCompatActivity() {
     lateinit var binding: ActivityViewMealPlansBinding
     lateinit var mealPlanViewModel: MealPlansViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityViewMealPlansBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        val repo = MealPlansRepositoryImpl()
        mealPlanViewModel = MealPlansViewModel(repo)

        // Get the mealPlanId from the Intent
        val mealPlanId = intent.getStringExtra("mealPlanId")
        if (mealPlanId == null) {
            Toast.makeText(this, "Meal Plan ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Retrieve meal plan details using the mealPlanId
        mealPlanViewModel.getMealPlanById(mealPlanId).observe(this, Observer { mealPlan ->
            if (mealPlan != null) {
                // Update the UI with meal plan details
                binding.mealType.setText(mealPlan.mealType)
                binding.mealName.setText(mealPlan.mealName)

                binding.mealProtein.setText(mealPlan.mealProtein) // Assuming this is a list formatted as a String
                binding.mealCalories.setText(mealPlan.mealCalories)
                Picasso.get().load(mealPlan.mealimageUrl).into(binding.imageBrowse)
            } else {
                Toast.makeText(this, "Meal Plan not found", Toast.LENGTH_SHORT).show()
            }
        })

        // Apply window insets safely
        findViewById<View>(R.id.main)?.let { mainView ->
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }
}