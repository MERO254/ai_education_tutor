package com.example.ai_education_tutor.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai_education_tutor.R
import com.example.ai_education_tutor.ui.theme.AI_education_tutorTheme

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFCFF))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // --- Header ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            Icon(Icons.Default.Notifications, contentDescription = null)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- Profile Info Section ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Image(
                    painter = painterResource(id = R.drawable.ic_profile_avatar), // Replace with your user image
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
                // Edit Icon Overlay
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFF3F1FF),
                    modifier = Modifier.size(28.dp).border(1.dp, Color.White, CircleShape)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.padding(6.dp),
                        tint = Color(0xFF9181F4)
                    )
                }
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column {
                Text(
                    text = "Aarav Sharma",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Always learning, always growing.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Pro Member Badge
                Surface(
                    color = Color(0xFFF3F1FF),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFF6C5CE7), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Pro Member", color = Color(0xFF6C5CE7), style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- Stats Card ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ProfileStatItem("16", "Courses Finished", R.drawable.ic_book, Color(0xFFF3F1FF), Color(0xFF9181F4))
                ProfileStatItem("48", "Learning Hours", R.drawable.ic_accesstime, Color(0xFFDDE7FF), Color(0xFF215AF6))
                ProfileStatItem("8", "Certificates", R.drawable.ic_verified, Color(0xFFE8F5E9), Color(0xFF4CAF50))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- Settings Options ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(24.dp))
                .padding(16.dp)
        ) {
            ProfileMenuRow(Icons.Default.Person, "Account Settings", "Manage your personal information", Color(0xFFF3F1FF), Color(0xFF9181F4))
            ProfileMenuRow(Icons.Default.Face, "Learning Preferences", "Customize your learning experience", Color(0xFFDDE7FF), Color(0xFF215AF6))
            ProfileMenuRow(Icons.Default.Notifications, "Notifications", "Manage your notification preferences", Color(0xFFE8F5E9), Color(0xFF4CAF50))
            ProfileMenuRow(Icons.Default.Call, "Help & Support", "Get help and find answers", Color(0xFFFFF4E5), Color(0xFFFF9800))
            ProfileMenuRow(Icons.Default.AccountBox, "Logout", "Sign out of your account", Color(0xFFFFEBEE), Color(0xFFF44336), showDivider = false)
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun ProfileStatItem(count: String, label: String, icon: Int, bgColor: Color, iconColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(shape = CircleShape, color = bgColor, modifier = Modifier.size(45.dp)) {
            Icon(painter = painterResource(icon), contentDescription = null, tint = iconColor, modifier = Modifier.padding(12.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = count, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
    }
}

@Composable
fun ProfileMenuRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    bgColor: Color,
    iconColor: Color,
    showDivider: Boolean = true
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(shape = CircleShape, color = bgColor, modifier = Modifier.size(40.dp)) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.padding(10.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
        }
        if (showDivider) {
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp, modifier = Modifier.padding(start = 56.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    AI_education_tutorTheme {
        ProfileScreen()
    }
}