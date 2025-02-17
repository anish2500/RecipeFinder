package com.example.recipefinder.repository

import com.example.recipefinder.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserRepositoryImpl: UserRepository {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    var database : FirebaseDatabase = FirebaseDatabase.getInstance()
    var ref : DatabaseReference = database.reference.child("users")



    override fun Login(email: String, password: String, callBack: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    callBack(true, "Login Successful")
                }else{
                    callBack(false, it.exception?.message.toString())
                }
            }
    }

    override fun signup(
        email: String,
        password: String,
        callBack: (Boolean, String, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    callBack(true, "Registration is successful", auth.currentUser?.uid.toString())
                }else{
                    callBack(false,it.exception?.message.toString(),"")
                }
            }
    }

    override fun addUserToDatabase(
        userId: String,
        userModel: UserModel,
        callBack: (Boolean, String) -> Unit
    ) {
        ref.child(userId).setValue(userModel).
        addOnCompleteListener{
            if (it.isSuccessful){
                callBack(true, "Registration succesful")
            }else{
                callBack(false, it.exception?.message.toString())
            }
        }
    }

    override fun forgetPassword(email: String, callBack: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    callBack(true, "Reset email is sent to $email")
                }else{
                    callBack(false, it.exception?.message.toString() )
                }
            }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser // returns the current user
    }

    override fun logout() {
        TODO("Not yet implemented")
    }

    override fun editProfile() {
        TODO("Not yet implemented")
    }


}