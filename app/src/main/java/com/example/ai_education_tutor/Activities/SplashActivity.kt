package com.example.ai_education_tutor.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.lifecycle.lifecycleScope
import com.example.ai_education_tutor.Activities.ui.theme.AI_education_tutorTheme
import com.example.ai_education_tutor.screens.SplashScreen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AI_education_tutorTheme {
                SplashScreen {
                    movetoActivity()
                }
            }
        }
    }

    private fun movetoActivity(){

        var auth = FirebaseAuth.getInstance()
        var userid = auth.currentUser?.uid

        if(userid == null){
            lifecycleScope.launch {
                delay(3000)
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }else{

            Toast.makeText(this, "continue to account ${auth.currentUser?.email}", Toast.LENGTH_SHORT).show()
            

            lifecycleScope.launch {
                delay(3000)
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    AI_education_tutorTheme {
        SplashScreen {  }
    }
}