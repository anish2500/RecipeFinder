package com.example.recipefinder.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.R
import com.example.recipefinder.adapter.RecipeAdapter
//import com.example.recipefinder.adapter.UserAdapter
import com.example.recipefinder.databinding.ActivityDashboardBinding
import com.example.recipefinder.repository.RecipeRepositoryImpl
import com.example.recipefinder.viewModel.RecipeViewModel
import com.example.recipefinder.viewModel.UserViewModel

class DashboardActivity : AppCompatActivity() {
    lateinit var binding: ActivityDashboardBinding

    lateinit var recipeViewModel :RecipeViewModel
    lateinit var adapter : RecipeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var repo = RecipeRepositoryImpl()
        recipeViewModel = RecipeViewModel(repo)
        adapter  = RecipeAdapter(this@DashboardActivity, ArrayList()) { selectedRecipe ->
            val intent = Intent(this@DashboardActivity, ViewRecipeActivity::class.java)
            intent.putExtra("recipeId", selectedRecipe.recipeId)
            startActivity(intent)
        }



        recipeViewModel.getAllRecipes()
        recipeViewModel.allRecipes.observe(this){

            it?.let {
                adapter.updateData(it)
            }
        }



        recipeViewModel.loading.observe(this) {
            loading ->
            if(loading){
                binding.progressBar.visibility = View.VISIBLE
            }else
            {
                binding.progressBar.visibility = View.GONE
            }
        }


        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)


        binding.floatingActionButton.setOnClickListener{
            var intent = Intent(this@DashboardActivity,
                AddRecipeActivity::class.java)
            startActivity(intent)
        }

        ItemTouchHelper(object:ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //onswipe ma matra changes lyauney ho on move ma ahile pardaina
                //after user swipe using productId kun index ma swipe garya xa tetai lanxa
                var recipeId = adapter.getRecipeId(viewHolder.adapterPosition)

                recipeViewModel.deleteRecipe(recipeId){
                        success, message->
                    if(success){
                        Toast.makeText(this@DashboardActivity, message, Toast.LENGTH_SHORT).show()

                    }else{
                        Toast.makeText(this@DashboardActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }


        })
            .attachToRecyclerView(binding.recyclerView)




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}