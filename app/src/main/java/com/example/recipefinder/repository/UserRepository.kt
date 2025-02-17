package com.example.recipefinder.repository

import com.example.recipefinder.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface UserRepository {


    fun Login(email:String, password:String,
              callBack:(Boolean,String)->Unit)

    /// signup ko call back ma extra string huney bhayo for user id
    fun signup(email:String, password: String,
               callBack: (Boolean, String, String) -> Unit)//for authentication second string stores user id


    fun addUserToDatabase(userId:String, userModel: UserModel
                          , callBack: (Boolean, String) -> Unit)//this is for real time database


    fun forgetPassword(email: String,
                       callBack: (Boolean, String) -> Unit)


    fun getCurrentUser(): FirebaseUser? // question mark is nullabel data can be present or not


    fun logout()


    fun editProfile()


}