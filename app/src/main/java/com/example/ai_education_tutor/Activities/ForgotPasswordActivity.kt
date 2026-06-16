package com.example.ai_education_tutor.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.ai_education_tutor.Activities.ui.theme.AI_education_tutorTheme
import com.example.ai_education_tutor.screens.ForgotPasswordScreen

class ForgotPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AI_education_tutorTheme {
                ForgotPasswordScreen {
                    backtoLogin()
                }
            }
        }
    }

    fun backtoLogin(){
        var intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}



@Preview(showBackground = true)
@Composable
fun ForgotPasswordActivityPreview() {
    AI_education_tutorTheme {
         ForgotPasswordScreen {  }
    }
}