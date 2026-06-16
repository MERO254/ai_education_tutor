package com.example.ai_education_tutor.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.Image
import androidx.compose.material.SnackbarHost
import com.example.ai_education_tutor.R
import com.example.ai_education_tutor.dataClasses.Course
import com.example.ai_education_tutor.fireBase.firebaseService
import com.example.ai_education_tutor.ui.theme.AI_education_tutorTheme
import kotlinx.coroutines.launch

// --- SAFE DESIGN THEME COLOR PARSERS ---
val Course.composeBgColor: Color
    get() = try {
        Color(android.graphics.Color.parseColor(this.bgColor))
    } catch (e: Exception) {
        Color(0xFFF3F1FF)
    }

val Course.composeTintColor: Color
    get() = try {
        Color(android.graphics.Color.parseColor(this.tintColor))
    } catch (e: Exception) {
        Color(0xFF6C5CE7)
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseScreen() {
    var courses by remember { mutableStateOf<List<Course>>(emptyList()) }
    var selectedFilter by remember { mutableStateOf("All") }

    //snackhoststate
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    // --- ADDED LOADING STATE ---
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        firebaseService.get_courses { coursesList ->
            courses = coursesList
            isLoading = false // Turn off loading when data is pulled
        }
    }

    val filteredCourses = when (selectedFilter) {
        "All" -> courses
        "In Progress" -> courses.filter {
            it.status == Course.statusValue.IN_PROGRESS.toString()
        }
        "Completed" -> courses.filter {
            it.status == Course.statusValue.COMPLETE.toString()
        }
        else -> courses
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFBFCFF))
                .padding(padding)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // --- Header ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", modifier = Modifier.size(28.dp), tint = Color.Black)
                Text(
                    text = "My Courses",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black,
                    fontSize = 20.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.Black)
                    Spacer(modifier = Modifier.width(12.dp))
                    Image(
                        painter = painterResource(id = R.drawable.img_robot_tutor),
                        contentDescription = "Profile",
                        modifier = Modifier.size(40.dp).clip(CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Search Bar ---
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Search courses...", color = Color.Gray, fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                trailingIcon = { Icon(Icons.Default.Settings, contentDescription = null, tint = Color.Black) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFFF0F0F0),
                    focusedBorderColor = Color(0xFF6C5CE7)
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // --- Filters ---
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilterChipItem("All", isSelected = selectedFilter == "All") { selectedFilter = "All" }
                FilterChipItem("In Progress", isSelected = selectedFilter == "In Progress", icon = Icons.Default.PlayArrow) { selectedFilter = "In Progress" }
                FilterChipItem("Completed", isSelected = selectedFilter == "Completed", icon = Icons.Default.CheckCircle) { selectedFilter = "Completed" }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Handle Content & Progress Display Modes ---
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .height(6.dp)
                                .clip(CircleShape),
                            color = Color(0xFF6C5CE7),
                            trackColor = Color(0xFFF3F1FF)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Loading courses...",
                            color = Color.Gray,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else if (filteredCourses.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No courses found in this category.", color = Color.Gray, fontSize = 14.sp)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredCourses) { course ->
                        CourseItemCard(
                            course = course,
                            onDeleteSuccess = { courseid ->
                                courses = courses.filter { it.courseId != courseid}
                                scope.launch {
                                    snackbarHostState.showSnackbar("course sucesfully deleted")
                                }
                            }
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun FilterChipItem(
    text: String,
    isSelected: Boolean,
    icon: ImageVector? = null,
    onClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color(0xFF6C5CE7) else Color.White,
        border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFF0F0F0)),
        modifier = Modifier.clickable { onClick() }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseItemCard(course: Course, onDeleteSuccess: (String) -> Unit) {
    val parsedBgColor = course.composeBgColor
    val parsedTintColor = course.composeTintColor
    val progressValue = 0.5f

    var showBottomSheet by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(64.dp)
                    .background(parsedBgColor, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = parsedTintColor
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 2.dp)
            ) {
                Surface(
                    color = parsedBgColor,
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(
                        text = course.status.ifBlank { "Active" },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        color = parsedTintColor
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = course.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 13.sp,
                    color = Color(0xFF1A1D26)
                )

                Text(
                    text = course.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 10.sp,
                    lineHeight = 13.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(0.95f)
                ) {
                    LinearProgressIndicator(
                        progress = { progressValue },
                        modifier = Modifier
                            .weight(1f)
                            .height(5.dp)
                            .clip(CircleShape),
                        color = parsedTintColor,
                        trackColor = parsedBgColor
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "${(progressValue * 100).toInt()}%",
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        color = parsedTintColor
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Options",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable {
                            showBottomSheet = true
                        }
                )

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(parsedBgColor)
                        .clickable { }
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (course.status == Course.statusValue.COMPLETE.toString())
                                Icons.Default.Check else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = parsedTintColor,
                            modifier = Modifier.size(10.dp)
                        )

                        Spacer(modifier = Modifier.width(3.dp))

                        Text(
                            text = course.status,
                            color = parsedTintColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }

    // Bottom Sheet Dialog for Delete Confirmation
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            containerColor = Color.White,
            scrimColor = Color.Black.copy(alpha = 0.4f)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // small drag hint spacing already handled by system

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Delete Course?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "This action cannot be undone",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    OutlinedButton(
                        onClick = { showBottomSheet = false },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            showBottomSheet = false

                            firebaseService.deletCourse(
                                courseId = course.courseId,
                                onSuccess = {
                                    onDeleteSuccess(course.courseId)
                                }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Delete", fontSize = 13.sp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
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