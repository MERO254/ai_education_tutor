package com.example.ai_education_tutor.screens

import android.util.Log
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai_education_tutor.R
import com.example.ai_education_tutor.courseGenerator.CourseGenerator
import com.example.ai_education_tutor.dataClasses.Course
import com.example.ai_education_tutor.dataClasses.Module
import com.example.ai_education_tutor.fireBase.firebaseService
import com.example.ai_education_tutor.ui.theme.AI_education_tutorTheme
import kotlinx.coroutines.launch

sealed interface CourseUiState {
    object Idle : CourseUiState
    object Loading : CourseUiState
    data class Success(val course: Course) : CourseUiState
    data class Error(val message: String) : CourseUiState
}

// --- EXTENSION HELPERS FOR THE MODULE TEXT STRINGS ---
// Add these helper extensions locally or define them directly inside your Module data class file!
val Module.composeTintColor: Color
    get() = try { Color(android.graphics.Color.parseColor(this.tintColor)) } catch(e: Exception) { Color(0xFF6C5CE7) }

val Module.composeBgColor: Color
    get() = try { Color(android.graphics.Color.parseColor(this.bgColor)) } catch(e: Exception) { Color(0xFFF3F1FF) }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AITopicGeneratorScreen(onBackClick: () -> Unit) {

    val scope = rememberCoroutineScope()
    var unitInput by remember { mutableStateOf("") }
    var uiState by remember { mutableStateOf<CourseUiState>(CourseUiState.Idle) }

    var originalGeneratedCourse by remember { mutableStateOf<Course?>(null) }
    val selectedModuleIds = remember { mutableStateListOf<String>() }
    var isSavingCourse by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = CircleShape, color = Color(0xFFF3F1FF), modifier = Modifier.size(32.dp)) {
                            Icon(painter = painterResource(id = R.drawable.ic_stars), contentDescription = null, tint = Color(0xFF6C5CE7), modifier = Modifier.padding(6.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("AI Course Generator", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFFBFCFF))
            )
        },
        containerColor = Color(0xFFFBFCFF)
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Control Panel Card ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = unitInput,
                            onValueChange = { unitInput = it },
                            label = { Text("Enter Unit Name") },
                            placeholder = { Text("e.g., Quantum Physics, Kotlin Basics...", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color(0xFFF0F0F0),
                                focusedBorderColor = Color(0xFF6C5CE7),
                                unfocusedLabelColor = Color.Gray,
                                focusedLabelColor = Color(0xFF6C5CE7),
                                unfocusedTextColor = Color.Black,
                                focusedTextColor = Color.Black
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // --- GENERATE BUTTON ---
                            Button(
                                onClick = {
                                    if (unitInput.isNotBlank()) {
                                        scope.launch {
                                            uiState = CourseUiState.Loading
                                            selectedModuleIds.clear()
                                            originalGeneratedCourse = null
                                            try {
                                                val generator = CourseGenerator(unit = unitInput)
                                                val course = generator.generateCompleteCourse()

                                                originalGeneratedCourse = course
                                                course.modules.forEach { selectedModuleIds.add(it.moduleId) }

                                                uiState = CourseUiState.Success(course)
                                            } catch (e: Exception) {
                                                uiState = CourseUiState.Error(e.localizedMessage ?: "Failed to generate course")
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f).height(46.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7)),
                                enabled = unitInput.isNotBlank() && uiState != CourseUiState.Loading
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Generate", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }

                            // --- SMART FILTERED SAVE BUTTON ---
                            Button(
                                onClick = {
                                    originalGeneratedCourse?.let { baseCourse ->
                                        scope.launch {
                                            isSavingCourse = true
                                            try {
                                                val filteredModules = baseCourse.modules.filter { it.moduleId in selectedModuleIds }
                                                val customizedCourse = baseCourse.copy(
                                                    modules = filteredModules,
                                                    totalLessons = filteredModules.sumOf { it.lessons.size }
                                                )

                                                firebaseService.saveCourse(
                                                    course = customizedCourse,
                                                    onSuccess = {
                                                        isSavingCourse = false
                                                        scope.launch {
                                                            snackbarHostState.showSnackbar(
                                                                message = "Course saved successfully!",
                                                                actionLabel = "Close"
                                                            )
                                                        }
                                                    },
                                                    onFailure = { error ->
                                                        isSavingCourse = false
                                                        scope.launch {
                                                            snackbarHostState.showSnackbar(
                                                                message = "${error.message}",
                                                                actionLabel = "Close"
                                                            )
                                                        }
                                                    }
                                                )
                                                Log.d("AITopicGenerator", "Saving customized path...")
                                            } catch (e: Exception) {
                                                isSavingCourse = false
                                                Log.e("AITopicGenerator", "Error uploading payload data", e)
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f).height(46.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4CAF50),
                                    disabledContainerColor = Color(0xFFE8F5E9)
                                ),
                                enabled = selectedModuleIds.isNotEmpty() && uiState !is CourseUiState.Loading
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Save (${selectedModuleIds.size})", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            // --- State Rendering Layout Stream ---
            when (val state = uiState) {
                is CourseUiState.Idle -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp), contentAlignment = Alignment.Center) {
                            Text("Enter a study unit above to begin your customized AI syllabus path.", color = Color.Gray, fontSize = 13.sp)
                        }
                    }
                }
                is CourseUiState.Loading -> {
                    item { GeneratingTopicsCard() }
                }
                is CourseUiState.Error -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                            Text(text = "Error: ${state.message}", color = Color.Red, fontSize = 14.sp)
                        }
                    }
                }
                is CourseUiState.Success -> {
                    val course = state.course
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFF3F1FF), modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.List, contentDescription = null, tint = Color(0xFF6C5CE7), modifier = Modifier.padding(4.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("AI-Generated Modules", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                            }

                            val isAllSelected = selectedModuleIds.size == course.modules.size
                            TextButton(
                                onClick = {
                                    if (isAllSelected) {
                                        selectedModuleIds.clear()
                                    } else {
                                        selectedModuleIds.clear()
                                        course.modules.forEach { selectedModuleIds.add(it.moduleId) }
                                    }
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (isAllSelected) "Deselect All" else "Select All (${course.modules.size})",
                                    color = Color(0xFF6C5CE7),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    items(course.modules) { module ->
                        val isChecked = selectedModuleIds.contains(module.moduleId)
                        ModuleListItem(
                            module = module,
                            isSelected = isChecked,
                            onSelectionChange = { selected ->
                                if (selected) {
                                    selectedModuleIds.add(module.moduleId)
                                } else {
                                    selectedModuleIds.remove(module.moduleId)
                                }
                            }
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }

    // FIX: Lifted bottom sheet system outside of the LazyColumn flow to display properly
    if (isSavingCourse) {
        CourseSavingLoadingSheet()
    }
}

@Composable
fun ModuleListItem(
    module: Module,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit
) {
    // FIX: Using the safely compiled extension color conversions here
    val tintColorParsed = module.composeTintColor
    val bgColorParsed = module.composeBgColor

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectionChange(!isSelected) },
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) tintColorParsed.copy(alpha = 0.4f) else Color(0xFFF8F9FB)
        ),
        shadowElevation = if (isSelected) 1.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Chapter Box
            Box(
                modifier = Modifier.size(60.dp).background(bgColorParsed, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_module),
                    contentDescription = null,
                    tint = tintColorParsed,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text Info Hierarchy Block
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = bgColorParsed, shape = RoundedCornerShape(4.dp)) {
                        Text(
                            text = "CH ${module.moduleOrder}",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            color = tintColorParsed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${module.lessons.size} Lessons",
                                color = Color.DarkGray,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(text = module.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = module.description, style = MaterialTheme.typography.bodySmall, color = Color.Gray, lineHeight = 16.sp)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Checkbox(
                checked = isSelected,
                onCheckedChange = { onSelectionChange(it) },
                colors = CheckboxDefaults.colors(
                    checkedColor = tintColorParsed,
                    uncheckedColor = Color(0xFFDCDDE1)
                )
            )
        }
    }
}

@Composable
fun GeneratingTopicsCard() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(2000, easing = LinearEasing)), label = ""
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { 0.7f },
                    modifier = Modifier.size(70.dp).rotate(rotation),
                    color = Color(0xFF6C5CE7),
                    strokeWidth = 6.dp,
                    trackColor = Color(0xFFF0F0F0)
                )
                Icon(painterResource(id = R.drawable.ic_stars), contentDescription = null, tint = Color(0xFF6C5CE7), modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text("Generating Topics...", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Text(
                    "Analyzing course content and creating structured modules for you.",
                    fontSize = 13.sp, color = Color.Gray, lineHeight = 18.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseSavingLoadingSheet(
    onDismissRequest: () -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(confirmValueChange = { false }),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = Color.White,
        scrimColor = Color.Black.copy(alpha = 0.4f),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp, bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(54.dp),
                color = Color(0xFF4CAF50),
                strokeWidth = 5.dp,
                trackColor = Color(0xFFF0F0F0)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Saving Your Path...",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    text = "Uploading course details, structured lessons, and generated quiz parameters safely to your workspace.",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    lineHeight = 18.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}