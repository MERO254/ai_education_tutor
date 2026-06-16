package com.example.ai_education_tutor.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.ai_education_tutor.Activities.ui.theme.AI_education_tutorTheme
import com.example.ai_education_tutor.screens.AITopicGeneratorScreen
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay

class AITopicGenerator_Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AI_education_tutorTheme {
                AITopicGeneratorScreen {
                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    AI_education_tutorTheme {
        AITopicGeneratorScreen {  }
    }
}