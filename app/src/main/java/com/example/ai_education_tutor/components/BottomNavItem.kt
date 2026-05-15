package com.example.ai_education_tutor.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ai_education_tutor.R

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: Int
){
    object Home: BottomNavItem(
        route = "home",
        title = "Home",
        icon = R.drawable.ic_home
    )

    object Course: BottomNavItem(
        route = "courses",
        title = "My Courses",
        icon = R.drawable.ic_course
    )

    object AI_tutor: BottomNavItem(
        route = "AI_tutor",
        title = "AI Tutor",
        icon = R.drawable.ic_ai
    )

    object Profile: BottomNavItem(
        route = "profile",
        title = "Profile",
        icon = R.drawable.ic_profile
    )
}