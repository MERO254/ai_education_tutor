package com.example.ai_education_tutor.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ai_education_tutor.Activities.ui.theme.AI_education_tutorTheme
import com.example.ai_education_tutor.screens.RegisterScreen
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AI_education_tutorTheme {
                RegisterScreen(
                    onBackClick = {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()

                    },
                    onSignUpSuccess = {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()


                    },
                    onLoginClick = {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()

                    }
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    AI_education_tutorTheme {
        RegisterScreen(
            onBackClick = {},
            onLoginClick = {},
            onSignUpSuccess = {}
        )
    }
}