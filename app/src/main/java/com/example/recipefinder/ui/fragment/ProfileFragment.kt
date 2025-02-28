package com.example.recipefinder.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.recipefinder.R
import com.example.recipefinder.databinding.FragmentProfileBinding
import com.example.recipefinder.repository.UserRepositoryImpl
import com.example.recipefinder.ui.activity.LoginActivity
import com.example.recipefinder.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var userFirstNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var userRepository: UserRepositoryImpl
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        userRepository = UserRepositoryImpl()

        userEmailTextView = binding.userEmailTextView
        userFirstNameTextView = binding.userFirstNameTextView

        // Display user information
        val currentUser = auth.currentUser
        if (currentUser != null) {
            userEmailTextView.text = "Email: ${currentUser.email}"
            userRepository.getUserData(currentUser.uid) { userModel ->
                if (userModel != null) {
                    userFirstNameTextView.text = "UserName: ${userModel.firstName}"
                } else {
                    userFirstNameTextView.text = "UserName: Not Found"
                }
            }
        } else {
            userEmailTextView.text = "Not logged in"
            userFirstNameTextView.text = "UserName: N/A"
        }

        // Handle logout button click
        binding.logoutButton.setOnClickListener {
            auth.signOut()
            navigateToSignIn()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToSignIn() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish() // Close the current activity to prevent going back
    }
}