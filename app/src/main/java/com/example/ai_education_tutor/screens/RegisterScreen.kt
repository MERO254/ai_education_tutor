package com.example.ai_education_tutor.screens

import android.content.Intent
import android.os.Build
import android.util.Patterns // Added for email validation
import androidx.annotation.RequiresApi
import com.example.ai_education_tutor.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ai_education_tutor.Activities.MainActivity
import com.example.ai_education_tutor.ViewModels.AuthViewModel
import com.example.ai_education_tutor.dataClasses.User
import com.example.ai_education_tutor.fireBase.firebaseService
import com.example.ai_education_tutor.ui.theme.AI_education_tutorTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterScreen(onBackClick: () -> Unit, onSignUpSuccess: () -> Unit, onLoginClick:() -> Unit) {

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isAgreed by remember { mutableStateOf(false) }

    // State parameters for showing/hiding password values
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    val viewmodel: AuthViewModel = viewModel()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewmodel.uiEvent.collectLatest { message ->
            snackbarHostState.showSnackbar(message, actionLabel = "Dismiss")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top bar
            Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            }

            // Robot illustration
            Image(
                painter = painterResource(id = R.drawable.ic_tutor_robot2),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title section
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = "Join AI Tutor and start your learning journey",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Input fields with visibility states wired up
            RegisterTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = "Full Name",
                icon = Icons.Default.Person
            )
            Spacer(modifier = Modifier.height(16.dp))

            RegisterTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                icon = Icons.Default.Email
            )
            Spacer(modifier = Modifier.height(16.dp))

            RegisterTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                icon = Icons.Default.Lock,
                isPassword = true,
                isPasswordVisible = isPasswordVisible,
                onVisibilityToggle = { isPasswordVisible = !isPasswordVisible }
            )
            Spacer(modifier = Modifier.height(16.dp))

            RegisterTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm Password",
                icon = Icons.Default.Lock,
                isPassword = true,
                isPasswordVisible = isConfirmPasswordVisible,
                onVisibilityToggle = { isConfirmPasswordVisible = !isConfirmPasswordVisible }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Terms and conditions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isAgreed,
                    onCheckedChange = { isAgreed = it },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF215AF6))
                )

                Text(text = "I agree to the ", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "Terms & Conditions",
                    color = Color(0xFF215AF6),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.clickable { /* show terms */ }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sign up button
            Button(
                enabled = !isLoading,
                onClick = {
                    // 1. Check if all entries are filled
                    if (fullName.trim().isEmpty() || email.trim().isEmpty() ||
                        password.isEmpty() || confirmPassword.isEmpty()) {
                        scope.launch { snackbarHostState.showSnackbar("Please enter all the credentials", actionLabel = "Dismiss") }
                        return@Button
                    }

                    // 2. Validate Email structure formatting
                    if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
                        scope.launch { snackbarHostState.showSnackbar("Please enter a valid email address", actionLabel = "Dismiss") }
                        return@Button
                    }

                    // 3. Match Passwords
                    if (password != confirmPassword) {
                        scope.launch { snackbarHostState.showSnackbar("Passwords do not match", actionLabel = "Dismiss") }
                        return@Button
                    }

                    // 4. Force check T&C
                    if (!isAgreed) {
                        scope.launch { snackbarHostState.showSnackbar("Please agree to the Terms & Conditions", actionLabel = "Dismiss") }
                        return@Button
                    }

                    // Everything is clean! Proceed with network registration state
                    isLoading = true
                    viewmodel.performSignUp(
                        email = email.trim(),
                        password = password,
                        onSignUpSuccess = {
                            onSignUpSuccess()

                            val userid = FirebaseAuth.getInstance().currentUser?.uid!!
                            val user = User(
                                userId = userid,
                                email = email,
                                fullName = fullName,
                                isProMember = false,
                                profilePictureUrl = "",
                                learningPreferences = emptyList()
                            )

                            //save user credentils
                            firebaseService.saveUserCredentials(user)
                            isLoading = false
                        },
                        onFailure = {
                            isLoading = false
                        }
                    )
                    // Note: If registration fails inside AuthViewModel, its internal callback must run onSignUpFailure to set isLoading = false.
                    // To handle failure tracking here safely, use your shared flow event handler at the top.
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF215AF6))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 3.dp
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Sign Up", fontSize = 18.sp, color = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer
            Row(modifier = Modifier.padding(bottom = 24.dp)) {
                Text("Already have an account? ", color = Color.Gray)
                Text(
                    text = "Log In",
                    color = Color(0xFF215AF6),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
fun RegisterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onVisibilityToggle: () -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = null, tint = Color.Gray) },
        trailingIcon = {
            if (isPassword) {
                // Wrap the eye icon in an IconButton to handle user taps
                IconButton(onClick = onVisibilityToggle) {
                    Icon(
                        painter = painterResource(
                            id = if (isPasswordVisible) R.drawable.ic_eye else R.drawable.ic_eye_off
                            // Make sure you have an R.drawable.ic_eye asset for the open state!
                        ),
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                        tint = Color.Gray
                    )
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        // Swap visual representation dynamically based on boolean flag state
        visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedBorderColor = Color(0xFF215AF6)
        )
    )

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    AI_education_tutorTheme {
        RegisterScreen(
            onLoginClick = {},
            onSignUpSuccess = {},
            onBackClick = {}
        )
    }
}