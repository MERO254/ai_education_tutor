package com.example.ai_education_tutor.Activities

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.ai_education_tutor.components.BottomNavBar
import com.example.ai_education_tutor.navigation.AppNavigation
import com.example.ai_education_tutor.ui.theme.AI_education_tutorTheme
import java.util.jar.Manifest

class MainActivity : ComponentActivity() {


    val requestNotificationPermisioLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if(isGranted){
            Log.d("notification","notification permission granted")
            setNotificationPrefrence(this,true)
        }else{
            Log.d("notification","notification permission not granted")
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        checkAndRequestNotificationPermision()

        setContent {
            AI_education_tutorTheme {
//                AppNavigation()

                val view = LocalView.current

                if(!view.isInEditMode){
                    DisposableEffect(view) {
                        val window = (view.context as Activity).window

                        val insertContoller = WindowCompat.getInsetsController(window, view)
                        insertContoller.isAppearanceLightStatusBars = false

                        onDispose {  }
                    }
                }

                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = Color.White
                ) {
                    AppNavigation()
                }
            }
        }
    }

    fun checkAndRequestNotificationPermision(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){

            when{
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED ->{
                    Log.d("notification", "notification already granted")
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS) ->{
                    Log.d("notifications","notifications help keep you updated")
                    requestNotificationPermisioLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }

                else ->{
                    requestNotificationPermisioLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }

        }else{
            Log.d("notification","not run time permisions needed for this version")
        }
    }
}


private fun setNotificationPrefrence(context: Context,isEnabled: Boolean){
    val prefrence = context.getSharedPreferences("app_setting", Context.MODE_PRIVATE)
    prefrence.edit().putBoolean("notification_enabled",isEnabled).apply()
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AI_education_tutorTheme {
        AppNavigation()
    }
}