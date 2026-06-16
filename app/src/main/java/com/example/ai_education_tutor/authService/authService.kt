package com.example.ai_education_tutor.authService

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.ai_education_tutor.Activities.MainActivity
import com.example.ai_education_tutor.fireBase.firebaseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class authService() {

    val auth = FirebaseAuth.getInstance()

    fun login(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit){

        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
            Log.d("auth","login successful")
            onSuccess()
        }.addOnFailureListener {failure ->
            Log.d("auth","login failure: ${failure.message}")
            onFailure(failure.message?: "signin failed")
        }

    }

    fun signup(email: String,password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit){

        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
            Log.d("auth","signup successful")
            onSuccess()
        }.addOnFailureListener {
            Log.d("auth","signup failure: ${it.message}")
            onFailure(it.message?:"sign up failed")
        }

    }

    fun logout(){
        auth.signOut()
    }

    fun loginWithGoogle(idToken: String,context: Context, onSuccess: () -> Unit, onFailure: (String) -> Unit){

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnSuccessListener { result ->
                Log.d("auth","goodle signin siccessful")
                var intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
                onSuccess
            }
            .addOnFailureListener { e ->
                Log.e("auth","google signin failed")
                onFailure(e.message?: "google sign in failure")
            }

    }


    fun forgotPassword(email: String, onSuccess: () -> Unit, onFailure: (String) -> Unit){

        auth.sendPasswordResetEmail(email).addOnSuccessListener {
            Log.d("auth","password reset sent")
            onSuccess()
        }.addOnFailureListener {
            Log.d("auth","password reset failure: ${it.message}")
            onFailure(it.message?: "password reset failed")
        }

    }

}