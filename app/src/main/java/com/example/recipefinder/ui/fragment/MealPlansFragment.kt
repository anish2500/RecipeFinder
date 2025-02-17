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

import com.example.recipefinder.adapter.MealPlansAdapter
import com.example.recipefinder.databinding.FragmentMealPlansBinding

import com.example.recipefinder.model.MealPlansModel
import com.example.recipefinder.repository.MealPlansRepositoryImpl
import com.example.recipefinder.ui.activity.AddMealPlansActivity
import com.example.recipefinder.ui.activity.ViewMealPlansActivity

import com.example.recipefinder.viewModel.MealPlansViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MealPlansFragment : Fragment() {

     var _binding: FragmentMealPlansBinding? = null
     val binding get() = _binding!!

    lateinit var mealPlanViewModel: MealPlansViewModel
     lateinit var adapter: MealPlansAdapter

     lateinit var searchView : SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealPlansBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = MealPlansRepositoryImpl()
        mealPlanViewModel = MealPlansViewModel(repo)

        // Setup search view
        searchView = binding.searchView
        setupSearchView(searchView)

        adapter = MealPlansAdapter(requireContext(), ArrayList()) { mealPlan ->
            navigateToViewMealPlan(mealPlan)
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mealPlanViewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        mealPlanViewModel.getAllMealPlans()

        mealPlanViewModel.allMealPlans.observe(viewLifecycleOwner) { mealPlans ->
            mealPlans?.let {
                adapter.updateData(it)
            }
        }

        val fab: FloatingActionButton = view.findViewById(R.id.goToAdd)
        fab.setOnClickListener {
            val intent = Intent(requireContext(), AddMealPlansActivity::class.java)
            startActivity(intent)
        }

        // Swipe-to-delete feature
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val mealPlanId = adapter.getMealPlanId(viewHolder.adapterPosition)

                mealPlanViewModel.deleteMealPlan(mealPlanId) { success, message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }).attachToRecyclerView(binding.recyclerView)
    }

    override fun onResume() {
        super.onResume()
        mealPlanViewModel.getAllMealPlans()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToViewMealPlan(mealPlan: MealPlansModel) {
        val intent = Intent(requireContext(), ViewMealPlansActivity::class.java)
        intent.putExtra("mealPlanId", mealPlan.mealPlanId)
        startActivity(intent)
    }

    private fun setupSearchView(searchView: SearchView) {
        this.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchMealPlans(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchMealPlans(it) }
                return true
            }
        })
    }

    private fun searchMealPlans(query: String) {
        val filteredList = mealPlanViewModel.searchMealPlans(query)
        adapter.updateData(filteredList)
    }
}
