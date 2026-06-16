package com.example.ai_education_tutor.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai_education_tutor.R
import com.example.ai_education_tutor.ui.theme.AI_education_tutorTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    // --- Animation States ---
    val scale = remember { Animatable(0.6f) }
    val alpha = remember { Animatable(0f) }

    // Trigger animations and handle navigation timing
    LaunchedEffect(Unit) {
        // Run animations concurrently using launch blocks
        val scaleJob = kotlin.concurrent.thread {
            // Running animations side-by-side cleanly
        }

        // Animate logo and content popping in simultaneously
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800)
        )

        // Wait for a brief moment, then navigate to Dashboard/Login
        delay(1800)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF4E73DF), // Light Blue
                        Color(0xFF224ABE)  // Deep Blue
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Star decorative backgrounds mimicking your AI Topic Header patterns
        SparkleBackgroundDecorations(modifier = Modifier.fillMaxSize().alpha(0.3f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            // FIX: Using graphicsLayer avoids structural preview clip-out bugs
            modifier = Modifier.graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value,
                alpha = alpha.value
            )
        ) {
            // Main Branding Visual (The Robot)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(160.dp)
            ) {
                // Outer clean glowing circle
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.15f),
                    modifier = Modifier.fillMaxSize()
                ) {}

                // Inner solid containment circle
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier.size(130.dp),
                    shadowElevation = 8.dp
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_robot_tutor),
                        contentDescription = "AI Tutor Logo",
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                    )
                }

                // Small AI Sparkle badge overlapping the central brand bubble
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF00F2FE),
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_stars),
                        contentDescription = null,
                        tint = Color(0xFF224ABE),
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Typographic Identity
            Text(
                text = "AI Tutor",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Learn Smart. Grow Faster.",
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SparkleBackgroundDecorations(modifier: Modifier) {
    Box(modifier = modifier) {
        Icon(
            painter = painterResource(id = R.drawable.ic_stars),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.TopStart)
                .padding(start = 40.dp, top = 80.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_stars),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.BottomEnd)
                .padding(end = 60.dp, bottom = 120.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplahScreenPreview() {
    AI_education_tutorTheme {
        SplashScreen{}
    }
}