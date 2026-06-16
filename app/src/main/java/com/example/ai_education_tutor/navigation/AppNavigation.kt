package com.example.ai_education_tutor.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ai_education_tutor.components.BottomNavBar
import com.example.ai_education_tutor.screens.AI_TutorScreen
import com.example.ai_education_tutor.screens.CourseScreen
import com.example.ai_education_tutor.screens.HomeScreen
import com.example.ai_education_tutor.screens.ProfileScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),

        containerColor = Color.White,
        contentColor = Color.White,
        bottomBar = {
            BottomNavBar(navController)
        },
    ){ paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ){

            composable ("home"){
                HomeScreen()
            }

            composable("courses"){
                CourseScreen()
            }

            composable("AI_tutor"){
                AI_TutorScreen(){

                }
            }

            composable("profile") {
                ProfileScreen()
            }

        }

    }
}