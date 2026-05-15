package com.example.ai_education_tutor.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai_education_tutor.R
import com.example.ai_education_tutor.ui.theme.AI_education_tutorTheme

// Data model for the courses
data class Course(
    val title: String,
    val description: String,
    val progress: Float,
    val status: String,
    val imageRes: Int,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseScreen() {
    val courses = listOf(
        Course("Advanced Mathematics", "Master calculus, linear algebra and more.", 0.68f, "In Progress", R.drawable.ic_maths_bg, Color(0xFFDDE7FF)),
        Course("Python Programming", "Learn Python from basics to advanced.", 0.42f, "In Progress", R.drawable.ic_python_bg, Color(0xFFE8F5E9)),
        Course("SQL for Data Analysis", "Query, analyze and visualize data effectively.", 0.55f, "In Progress", R.drawable.ic_sql_bg, Color(0xFFF3F1FF)),
        Course("JavaScript Essentials", "Build interactive websites with JavaScript.", 1.0f, "Completed", R.drawable.ic_js_bg, Color(0xFFFFF4E5))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFCFF)) // Very light blue-ish white
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // --- Header ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Menu, contentDescription = "Menu", modifier = Modifier.size(28.dp))
            Text(
                text = "My Courses",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    painter = painterResource(id = R.drawable.img_robot_tutor),
                    contentDescription = "Profile",
                    modifier = Modifier.size(35.dp).clip(CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Search Bar ---
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search courses...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
            trailingIcon = { Icon(Icons.Default.Settings, contentDescription = null, tint = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color(0xFFF0F0F0),
                focusedBorderColor = Color(0xFF215AF6)
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // --- Filters ---
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            FilterChipItem("All", isSelected = true)
            FilterChipItem("In Progress", isSelected = false, icon = Icons.Default.PlayArrow)
            FilterChipItem("Completed", isSelected = false, icon = Icons.Default.CheckCircle)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Course List ---
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(courses) { course ->
                CourseItemCard(course)
            }
        }
    }
}

@Composable
fun FilterChipItem(text: String, isSelected: Boolean, icon: ImageVector? = null) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color(0xFF6C5CE7) else Color.White,
        border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = if (isSelected) Color.White else Color(0xFF4CAF50)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = if (isSelected) Color.White else Color.Gray
            )
        }
    }
}

@Composable
fun CourseItemCard(course: Course) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Course Visual
            Box(
                modifier = Modifier
                    .size(85.dp)
                    .background(course.color, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = course.imageRes),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Badge
                Surface(
                    color = if (course.status == "Completed") Color(0xFFE8F5E9) else Color(0xFFF3F1FF),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = course.status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (course.status == "Completed") Color(0xFF4CAF50) else Color(0xFF9181F4),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = course.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = course.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Progress
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LinearProgressIndicator(
                        progress = { course.progress },
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(CircleShape),
                        color = if (course.status == "Completed") Color(0xFF4CAF50) else Color(0xFF9181F4),
                        trackColor = Color(0xFFF0F0F0)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${(course.progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (course.status == "Completed") Color(0xFF4CAF50) else Color(0xFF9181F4)
                    )
                }
            }

            // Resume Button
            IconButton(onClick = { /* Navigate to course */ }) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color(0xFF6C5CE7),
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoursePreview() {
    AI_education_tutorTheme {
        CourseScreen()
    }
}