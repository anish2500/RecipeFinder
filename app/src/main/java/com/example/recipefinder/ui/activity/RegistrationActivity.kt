package com.example.recipefinder.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.anew.utils.LoadingUtils
import com.example.recipefinder.R
import com.example.recipefinder.databinding.ActivityRegistrationBinding
import com.example.recipefinder.model.UserModel
import com.example.recipefinder.repository.UserRepositoryImpl
import com.example.recipefinder.viewModel.UserViewModel

class RegistrationActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegistrationBinding

    lateinit var userViewModel: UserViewModel
    lateinit var loadingUtils: LoadingUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistrationBinding.inflate(layoutInflater)

        setContentView(binding.root)

        loadingUtils = LoadingUtils(this)


        var repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)


        binding.logintext.setOnClickListener() {
            val intent = Intent(
                this@RegistrationActivity,
                LoginActivity::class.java
            )
            startActivity(intent)
        }

      

        binding.signUp.setOnClickListener {
            var email = binding.registerEmail.text.toString()
            var password = binding.registerPassword.text.toString()
            var firstName = binding.registerFname.text.toString()
            var lastName = binding.registerLName.text.toString()
            var address = binding.registerAddress.text.toString()
            var contact = binding.registerContact.text.toString()


            //login ko lagi banauney and forget password ni banauney xml file banayera
            userViewModel.Login(email, password) { success, message ->
                if (success) {
                    loadingUtils.dismiss()
                    Toast.makeText(this@RegistrationActivity, message, Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@RegistrationActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish() // Prevent going back to RegistrationActivity
                } else {
                    Toast.makeText(this@RegistrationActivity, message, Toast.LENGTH_SHORT).show()
                }
            }

            userViewModel.signup(email, password) { success, message, userId ->

                if (success) {
                    var userModel = UserModel(
                        userId.toString(), firstName, lastName, address, contact, email

                    )
                    userViewModel.addUserToDatabase(userId, userModel) { success, message ->
                        if (success) {
                            loadingUtils.dismiss()
                            Toast.makeText(
                                this@RegistrationActivity,
                                message, Toast.LENGTH_SHORT
                            ).show()


                        } else {
                            loadingUtils.dismiss()
                            Toast.makeText(
                                this@RegistrationActivity,
                                message, Toast.LENGTH_SHORT
                            ).show()

                        }


                    }

                }







                userViewModel.signup(email, password) { success, message, userId ->
                    if (success) {

                        var userModel = UserModel(
                            userId.toString(), firstName, lastName, address, contact, email
                        )
                        userViewModel.addUserToDatabase(userId, userModel) { success, message ->
                            if (success) {
                                Toast.makeText(
                                    this@RegistrationActivity, message, Toast.LENGTH_SHORT
                                ).show()

                            } else {
                                Toast.makeText(
                                    this@RegistrationActivity, message, Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    } else {
                        Toast.makeText(
                            this@RegistrationActivity,
                            message, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }
}