package com.example.recipefinder.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.recipefinder.R

class SplashActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)



        // Initialize shared preferences
        sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE)

        // Delay for the splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            // Check if the user is logged in
            val username: String = sharedPreferences.getString("username", "").toString()

            if (username.isEmpty()) {
                // If not logged in, navigate to LoginActivity
                val loginIntent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(loginIntent)
            } else {
                // If logged in, navigate to DashboardActivity
                val dashboardIntent = Intent(this@SplashActivity, DashboardActivity::class.java)
                startActivity(dashboardIntent)
            }

            // Finish the splash activity so it doesn't remain in the back stack
            finish()
        }, 1000)

        // Apply edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
