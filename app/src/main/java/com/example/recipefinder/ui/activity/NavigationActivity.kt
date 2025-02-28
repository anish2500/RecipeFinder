package com.example.recipefinder.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.recipefinder.R
import com.example.recipefinder.databinding.ActivityNavigationBinding
import com.example.recipefinder.ui.fragment.AddFragment
import com.example.recipefinder.ui.fragment.HomeFragment
import com.example.recipefinder.ui.fragment.MealPlansFragment
import com.example.recipefinder.ui.fragment.ProfileFragment

class NavigationActivity : AppCompatActivity() {
    lateinit var binding: ActivityNavigationBinding


    private fun replaceFragment(fragment: Fragment){
        val fragmentManager:FragmentManager = supportFragmentManager
        val fragmentTransaction:FragmentTransaction  = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.frameLayout.id,fragment)
        fragmentTransaction.commit()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        replaceFragment(HomeFragment())


        binding.bottomNavigation.setOnItemSelectedListener { menu->

            when(menu.itemId){
                R.id.home -> replaceFragment(HomeFragment())
                R.id.add -> replaceFragment(AddFragment())

                R.id.mealPlans -> replaceFragment(MealPlansFragment())
                R.id.profile -> replaceFragment(ProfileFragment())

                else ->{

                }

            }
            true
        }





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }
}