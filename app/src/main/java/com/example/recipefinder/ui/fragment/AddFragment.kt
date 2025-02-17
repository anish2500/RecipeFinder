

package com.example.recipefinder.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipefinder.R
import com.example.recipefinder.adapter.RecipeAdapter
import com.example.recipefinder.databinding.FragmentAddBinding
import com.example.recipefinder.model.RecipeModel
import com.example.recipefinder.repository.RecipeRepositoryImpl
import com.example.recipefinder.ui.activity.AddRecipeActivity
import com.example.recipefinder.ui.activity.ViewRecipeActivity
import com.example.recipefinder.viewModel.FavoritesViewModel
import com.example.recipefinder.viewModel.RecipeViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var adapter: RecipeAdapter

    private lateinit var searchView : SearchView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = RecipeRepositoryImpl()
        recipeViewModel = RecipeViewModel(repo)

        //for search purposes
        searchView = binding.searchView
        setupSearchView(searchView)


        adapter = RecipeAdapter(requireContext(), ArrayList()) { recipe ->
            navigateToViewRecipe(recipe)
        }




        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recipeViewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        recipeViewModel.getAllRecipes()

        recipeViewModel.allRecipes.observe(viewLifecycleOwner) { recipes ->
            recipes?.let {
                adapter.updateData(it)
            }
        }

        val fab: FloatingActionButton = view.findViewById(R.id.goToAdd)
        fab.setOnClickListener {
            val intent = Intent(requireContext(), AddRecipeActivity::class.java)
            startActivity(intent)
        }




        // Swipe-to-delete feature
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // No movement required
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val recipeId = adapter.getRecipeId(viewHolder.adapterPosition)

                recipeViewModel.deleteRecipe(recipeId) { success, message ->
                    if (success) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }).attachToRecyclerView(binding.recyclerView)
    }

    override fun onResume() {
        super.onResume()
        recipeViewModel.getAllRecipes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToViewRecipe(recipe: RecipeModel){
        val intent = Intent(requireContext(), ViewRecipeActivity::class.java)
        intent.putExtra("recipeId", recipe.recipeId)
        startActivity(intent)

    }


    //code for search view
    private fun setupSearchView(searchView: SearchView) {
        this.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchRecipes(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchRecipes(it) }
                return true
            }
        })
    }

    //code for search view
    private fun searchRecipes(query: String) {
        val filteredList = recipeViewModel.searchRecipes(query)  // Use the viewModel's searchRecipes method
        adapter.updateData(filteredList)  // Update the adapter with the filtered list
    }








}
