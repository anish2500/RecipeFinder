package com.example.recipefinder.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.recipefinder.model.UserModel
import com.example.recipefinder.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserViewModel(val repo : UserRepository)  {

    val userFirstName = MutableLiveData<String>()
    val userEmail = MutableLiveData<String>()

    fun Login(email:String, password:String,
              callBack:(Boolean,String)->Unit){
        repo.Login(email,password, callBack)

    }

    /// signup ko call back ma extra string huney bhayo for user id
    fun signup(email:String, password: String,
               callBack: (Boolean, String, String) -> Unit){
        repo.signup(email,password, callBack)

    }//for authentication second string stores user id


    fun addUserToDatabase(userId:String, userModel: UserModel
                          , callBack: (Boolean, String) -> Unit){
        repo.addUserToDatabase(userId, userModel, callBack)

    }//this is for real time database


    fun forgetPassword(email: String,
                       callBack: (Boolean, String) -> Unit){
        repo.forgetPassword(email, callBack)

    }


    fun getCurrentUser(): FirebaseUser?{
        return repo.getCurrentUser()

    } // question mark is null label  data can be present or not

    fun fetchUserDetails(userId: String, callBack: (Boolean, String, String) -> Unit) {
        repo.fetchUserDetails(userId) { success, firstName, email ->
            if (success) {
                userFirstName.value = firstName
                userEmail.value = email
            }
            callBack(success, firstName, email)
        }
    }


    fun logout(){
        FirebaseAuth.getInstance().signOut()


    }


    fun editProfile(){

    }




}