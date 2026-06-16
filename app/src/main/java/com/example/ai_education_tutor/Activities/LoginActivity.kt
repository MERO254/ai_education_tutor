package com.example.ai_education_tutor.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.example.ai_education_tutor.Activities.ui.theme.AI_education_tutorTheme
import com.example.ai_education_tutor.authService.authService
import com.example.ai_education_tutor.screens.LoginScreen
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {

    private val myAuthService = authService()
    private lateinit var credentialManager: CredentialManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Initialize the modern system Credential Manager
        credentialManager = CredentialManager.create(this)

        setContent {
            AI_education_tutorTheme {
                LoginScreen(
                    onLogin = {
                        navigateToMain()
                    },
                    onFogotpasClick = {
                        val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                        startActivity(intent)
                        finish()
                    },
                    onSignUpClicked = {
                        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                        startActivity(intent)
                        finish()
                    },
                    onGoogleSigninClicked = {
                        // 2. Fire the modern Google authentication sequence directly from the click
                        triggerModernGoogleAuth()
                    }
                )
            }
        }
    }

    private fun triggerModernGoogleAuth() {
        val webClient = "1080240816128-atdfec52bpcdk5sdukfgjfq41a46v41q.apps.googleusercontent.com"

        // Configure the single-tap Google ID option
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false) // Shows all Google accounts on device
            .setServerClientId(webClient)
            .setAutoSelectEnabled(false)
            .build()

        // Wrap it into a credential system request
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        // Launch asynchronous request inside the Activity lifecycle scope
        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    context = this@LoginActivity,
                    request = request
                )

                // Process the returned credential payload
                val credential = result.credential
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val idToken = googleIdTokenCredential.idToken

                    // Hand the token to your authService
                    myAuthService.loginWithGoogle(
                        idToken = idToken,
                        context = this@LoginActivity,
                        onSuccess = {
                            Log.d("AUTH_DIAGNOSIS", "SIGN IN SUCCESSFUL via Credential Manager")
                            Toast.makeText(this@LoginActivity, "Welcome!", Toast.LENGTH_SHORT).show()
//                            navigateToMain()
                        },
                        onFailure = { errorText ->
                            Log.e("AUTH_DIAGNOSIS", "Firebase rejected token: $errorText")
                            Toast.makeText(this@LoginActivity, "Auth Failed: $errorText", Toast.LENGTH_LONG).show()
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e("AUTH_DIAGNOSIS", "Credential Manager sheet failed or was cancelled: ${e.message}")
                Toast.makeText(this@LoginActivity, "Google Sign-In cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish() // Added finish() to clean up the login activity out of memory
    }
}