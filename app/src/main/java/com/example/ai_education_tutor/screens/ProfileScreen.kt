package com.example.ai_education_tutor.screens

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai_education_tutor.Activities.LoginActivity
import com.example.ai_education_tutor.R
import com.example.ai_education_tutor.authService.authService
import com.example.ai_education_tutor.fireBase.firebaseService
import com.example.ai_education_tutor.ui.theme.AI_education_tutorTheme
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen() {
    val context = LocalContext.current

    var username by remember { mutableStateOf("Loading...") }
    var membership by remember { mutableStateOf("Free Member") }
    var email by remember { mutableStateOf("") }

    var openSettingsDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var openPrefrencceDialog by remember {mutableStateOf(false)}

    var learningPrefrence  by remember { mutableStateOf(listOf<String>()) }

    var openNotificationDialog by remember {mutableStateOf(false)}
    var isNotificationEnable by remember {mutableStateOf(true)}

    var showSignOutDilaog by remember { mutableStateOf(false) }

//    //progress
//    var finishedcourses by remember { mutableStateOf(0) }
//    var learninghours by remember { mutableStateOf(0.0) }
//    var certificates by remember {mutableStateOf(0)}


    val progressList by progresTracking.progressList.collectAsState()

    val finishedcourses = progressList.count { it.completed }
    val learninghours = progressList.sumOf { it.studyHours.toDouble() }
    val certificates = progressList.sumOf { it.badgesEarned.size }

    //check notification status from prefrence
    var prefrence = context.getSharedPreferences("app_setting", Context.MODE_PRIVATE)
    val notificationstate = prefrence.getBoolean("notification_enabled",true)
    isNotificationEnable = notificationstate


    LaunchedEffect(Unit) {
        firebaseService.getUserCredentials { userCredentials ->
            username = userCredentials.fullName
            membership = if (userCredentials.isProMember) "Pro Member" else "Free Member"
            email = userCredentials.email
            learningPrefrence = userCredentials.learningPreferences
        }

    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color(0xFFFBFCFF)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.Black)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Profile Info Section ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar_image),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                    )
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFF3F1FF),
                        modifier = Modifier
                            .size(28.dp)
                            .border(1.dp, Color.White, CircleShape)
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
                        text = username,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "Always learning, always growing.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
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
                            Text(membership, color = Color(0xFF6C5CE7), style = MaterialTheme.typography.labelMedium)
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
                    ProfileStatItem("${finishedcourses}", "Courses Finished", R.drawable.ic_book, Color(0xFFF3F1FF), Color(0xFF9181F4))
                    ProfileStatItem("${learninghours}", "Learning Hours", R.drawable.ic_accesstime, Color(0xFFDDE7FF), Color(0xFF215AF6))
                    ProfileStatItem("${certificates}", "Certificates", R.drawable.ic_verified, Color(0xFFE8F5E9), Color(0xFF4CAF50))
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
                ProfileMenuRow(Icons.Default.Person, "Account Settings", "Manage your personal information", Color(0xFFF3F1FF), Color(0xFF9181F4), onClick = {
                    openSettingsDialog = true
                })
                ProfileMenuRow(Icons.Default.Face, "Learning Preferences", "Customize your learning experience", Color(0xFFDDE7FF), Color(0xFF215AF6), onClick = {openPrefrencceDialog = true})
                ProfileMenuRow(Icons.Default.Notifications, "Notifications", "Manage your notification preferences", Color(0xFFE8F5E9), Color(0xFF4CAF50), onClick = {
                    openNotificationDialog = true
                })
                ProfileMenuRow(Icons.Default.Call, "Help & Support", "Get help and find answers", Color(0xFFFFF4E5), Color(0xFFFF9800), onClick = {})
                ProfileMenuRow(Icons.Default.AccountBox, "Logout", "Sign out of your account", Color(0xFFFFEBEE), Color(0xFFF44336), showDivider = false, onClick = { showSignOutDilaog = true })

                //---- Account setting dialog ----
                AccountSettingsBottomSheet(
                    showSheet = openSettingsDialog,
                    onDismissRequest = { openSettingsDialog = false },
                    currentEmail = email,
                    subscriptionTier = membership,
                    onUpdateEmail = { newEmail, password  ->
                        scope.launch {
                            openSettingsDialog = false
                            val isSuccess = firebaseService.changeUserEmail(password, newEmail)

                            if (isSuccess) {
                                snackbarHostState.showSnackbar(
                                    message = "Verification link sent to $newEmail!",
                                    duration = SnackbarDuration.Long
                                )
                            } else {
                                snackbarHostState.showSnackbar(
                                    message = "Update failed. Check your password and try again.",
                                    actionLabel = "Dismiss",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    onResetPassword = {
                        authService().forgotPassword(
                            email = email,
                            onSuccess = {
                                scope.launch {
                                    openSettingsDialog = false
                                    snackbarHostState.showSnackbar("Reset email sent successfully!")
                                }
                            },
                            onFailure = { error ->
                                scope.launch {
                                    snackbarHostState.showSnackbar("Error: ${error?: "Failed to reset"}")
                                }
                            }
                        )
                    }
                )

                //---- learning prefrence ----
                LearningPreferencesBottomSheet(
                    showSheet = openPrefrencceDialog,
                    onDismissRequest = { openPrefrencceDialog= false },
                    preferences = learningPrefrence,
                    onAddPreference = { newPref ->
                        if (newPref.isNotBlank() && !learningPrefrence.contains(newPref)) {

                            val updatedList = learningPrefrence + newPref
                            learningPrefrence = updatedList

                            scope.launch {
                                firebaseService.updateCredentials(
                                    key = "learningPreferences",
                                    value = updatedList,
                                    onSuccess = {
                                        Log.d("Learning Prefrence","learnign prefrence added")
                                    },
                                    onFailure = {

                                    }
                                )
                            }
                        }
                    },
                    onRemovePreference = { prefToRemove ->

                        val updatedList = learningPrefrence - prefToRemove
                        learningPrefrence = updatedList

                        scope.launch {
                            firebaseService.updateCredentials(
                                key = "learningPreferences",
                                value = updatedList,
                                onSuccess = {
                                    Log.d("Learning Prefrence","learnign prefrence added")
                                },
                                onFailure = {

                                }
                            )
                        }
                    }
                )

                //notification dialog
                if(openNotificationDialog){
                    NotificationBottomSheet(
                        isNotificationEnable = isNotificationEnable,
                        onToggleNotification = {enable ->
                            isNotificationEnable = enable
                            //update prefrence
                            var prefrence = context.getSharedPreferences("app_setting", Context.MODE_PRIVATE)
                            prefrence.edit().putBoolean("notification_enabled",enable).apply()
                        },
                        onDismissRequest = {
                            openNotificationDialog = false
                        }
                    )
                }

                //signout alert dialog
                if(showSignOutDilaog){
                    signOutAlertDialog(
                        onConfirm = {
                            logout(context)
                            showSignOutDilaog = false
                        },
                        onDismiss = {
                            showSignOutDilaog = false
                        }
                    )
                }


            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ProfileStatItem(count: String, label: String, icon: Int, bgColor: Color, iconColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(shape = CircleShape, color = bgColor, modifier = Modifier.size(45.dp)) {
            Icon(painter = painterResource(icon), contentDescription = null, tint = iconColor, modifier = Modifier.padding(12.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = count, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
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
    showDivider: Boolean = true,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(shape = CircleShape, color = bgColor, modifier = Modifier.size(40.dp)) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.padding(10.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
        }
        if (showDivider) {
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp, modifier = Modifier.padding(start = 56.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsBottomSheet(
    showSheet: Boolean,
    onDismissRequest: () -> Unit,
    currentEmail: String,
    subscriptionTier: String,
    onUpdateEmail: (email:String, password:String) -> Unit,
    onResetPassword: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var isEditingEmail by remember { mutableStateOf(false) }
    var emailInput by remember { mutableStateOf(currentEmail) }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(showSheet, currentEmail) {
        if (showSheet) {
            isEditingEmail = false
        }
        emailInput = currentEmail
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            containerColor = Color.White,
            dragHandle = { BottomSheetDefaults.DragHandle(color = Color(0xFFE2E8F0)) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp, bottom = 32.dp)
                    .navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_manage),
                        contentDescription = null,
                        tint = Color(0xFF215AF6),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Account Credentials",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFF)),
                    border = BorderStroke(1.dp, Color(0xFFEDF2F8)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Email Address", fontSize = 12.sp, color = Color.Gray)
                                if (!isEditingEmail) {
                                    Text(
                                        text = currentEmail,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.Black
                                    )
                                }
                            }

                            TextButton(
                                onClick = {
                                    if (isEditingEmail) {
                                        onUpdateEmail(emailInput, password)
                                        isEditingEmail = false
                                    } else {
                                        isEditingEmail = true
                                    }
                                }
                            ) {
                                Text(
                                    text = if (isEditingEmail) "Save" else "Change",
                                    color = Color(0xFF215AF6),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        AnimatedVisibility(visible = isEditingEmail) {
                            OutlinedTextField(
                                value = emailInput,
                                onValueChange = { emailInput = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF215AF6),
                                    unfocusedBorderColor = Color(0xFFE2E8F0)
                                )
                            )
                        }

                        AnimatedVisibility(visible = isEditingEmail) {
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                placeholder = {Text("Enter current password")},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF215AF6),
                                    unfocusedBorderColor = Color(0xFFE2E8F0)
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFF)),
                    border = BorderStroke(1.dp, Color(0xFFEDF2F8)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Password", fontSize = 12.sp, color = Color.Gray)
                            Text("••••••••••••", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                        }
                        Button(
                            onClick = onResetPassword,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_lockreset),
                                contentDescription = null,
                                tint = Color.DarkGray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Reset", color = Color.DarkGray, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFF)),
                    border = BorderStroke(1.dp, Color(0xFFEDF2F8)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Subscription Tier", fontSize = 12.sp, color = Color.Gray)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 2.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_stars),
                                    contentDescription = null,
                                    tint = Color(0xFF6C5CE7),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = subscriptionTier,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }

                        Surface(
                            color = Color(0xFFF3F1FF),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Active",
                                color = Color(0xFF6C5CE7),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningPreferencesBottomSheet(
    showSheet: Boolean,
    onDismissRequest: () -> Unit,
    preferences: List<String>,
    onAddPreference: (String) -> Unit,
    onRemovePreference: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var preferenceInput by remember { mutableStateOf("") }

    // Clear input field when sheet closes or opens
    LaunchedEffect(showSheet) {
        if (showSheet) preferenceInput = ""
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            containerColor = Color.White,
            dragHandle = { BottomSheetDefaults.DragHandle(color = Color(0xFFE2E8F0)) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp, bottom = 32.dp)
                    .navigationBarsPadding()
            ) {
                // --- Title Header ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = null,
                        tint = Color(0xFF215AF6),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Learning Preferences",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Add or remove topics you are interested in exploring.",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                // --- Input Field Section ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = preferenceInput,
                        onValueChange = { preferenceInput = it },
                        placeholder = { Text("e.g., Mobile Development", color = Color.LightGray) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF215AF6),
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {
                            if (preferenceInput.isNotBlank()) {
                                onAddPreference(preferenceInput.trim())
                                preferenceInput = "" // reset field
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF215AF6)),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Preference", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- Preferences List Cards ---
                Text(
                    text = "Your Saved Preferences",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (preferences.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No preferences added yet.",
                            color = Color.LightGray,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    // Wrap with a Column or FlowRow depending on display preference.
                    // This creates clean individual chip-cards that scroll nicely.
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        preferences.forEach { preference ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFF)),
                                border = BorderStroke(1.dp, Color(0xFFEDF2F8)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = preference,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    )

                                    IconButton(
                                        onClick = { onRemovePreference(preference) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Remove item",
                                            tint = Color(0xFFF44336),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationBottomSheet(
    isNotificationEnable: Boolean,
    onToggleNotification: (Boolean) -> Unit,
    onDismissRequest: () -> Unit
){

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = {},
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Notification Settings",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "chooese whether you want tp recieve notificaions from this application",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.fillMaxWidth())

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if(isNotificationEnable) "Notification is On" else "Notifcation is off",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f),
                    color = Color.Black
                )

                Switch(
                    checked = isNotificationEnable,
                    onCheckedChange = { newValue ->
                        onToggleNotification(newValue)
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }



}


@Composable
private fun signOutAlertDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
){

    AlertDialog(
        onDismissRequest = {onDismiss()},
        title = {Text(text = "Sign Out", color = Color.Black)},
        text = {
            Text(text = "are you sure you want to sign out?")
        },
        confirmButton = {
            TextButton(onClick = {onConfirm()}) {
                Text(text = "Ok", color = Color.Blue)
            }
        },
        dismissButton = {
            TextButton(onClick = {onDismiss()}) {
                Text(text = "Cancel", color = Color.Blue)
            }
        },
        containerColor = Color.White

    )

}

fun logout(context: Context) {
    authService().logout()
    val intent = Intent(context, LoginActivity::class.java)
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    AI_education_tutorTheme {
        ProfileScreen()
    }
}