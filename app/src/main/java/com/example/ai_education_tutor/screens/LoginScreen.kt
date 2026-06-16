package com.example.ai_education_tutor.screens

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ai_education_tutor.Activities.MainActivity
import com.example.ai_education_tutor.Activities.RegisterActivity
import com.example.ai_education_tutor.R
import com.example.ai_education_tutor.ViewModels.AuthViewModel
import com.example.ai_education_tutor.ui.theme.AI_education_tutorTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLogin: () -> Unit, onFogotpasClick: () -> Unit,onSignUpClicked: () -> Unit, onGoogleSigninClicked: () -> Unit){

    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    val viewModel: AuthViewModel = viewModel()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { message ->
            snackbarHostState.showSnackbar(
                message,
                actionLabel = "Dismiss"
            )
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

            Spacer(modifier = Modifier.height(20.dp))

            // Header section (logo and Robot)
            Box(modifier = Modifier.fillMaxWidth()){
                Column(
                    modifier = Modifier.align(Alignment.TopStart),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_ai_logo),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "AI Tutor",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }

                    Text(
                        text = "Learn Smarter, Achieve More",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_tutor_robot2),
                    contentDescription = null,
                    modifier = Modifier
                        .size(180.dp)
                        .align(Alignment.TopEnd)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Welcome text
            Text(
                text = "Welcome Back!",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Start
            )

            Text(
                text = "Sign in to continue your learning journey",
                modifier = Modifier.fillMaxWidth(),
                color = Color.Gray,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Input fields
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                placeholder = { Text("you@example.com") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                placeholder = { Text("Enter your Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = { Icon(painterResource(id = R.drawable.ic_eye_off), contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )

            TextButton(
                onClick = { onFogotpasClick() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Forgot Password?", color = Color(0xFF215AF6))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Fixed Login Button Logic ---
            Button(
                enabled = !isLoading,
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()){
                        isLoading = true // Only set loading to true when input is valid

                        viewModel.performLogin(
                            email = email,
                            password = password,
                            onLoginSuccess = {
                                onLogin()
                                isLoading = false
                            },
                            onLoginFailure = {
                                isLoading = false
                            }
                        )
                    } else {
                        // If fields are empty, keep isLoading as false so they can fix input and retry
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Please enter all the fields",
                                actionLabel = "Dismiss"
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF215AF6),
                    contentColor = Color.White
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 3.dp
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Login", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Divider
            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f), color = Color(0xFFEEEEEE))

                Text(
                    text = "or continue with",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )

                Divider(modifier = Modifier.weight(1f), color = Color(0xFFEEEEEE))
            }

            // Google Sign In
            OutlinedButton(
                onClick = {
                    onGoogleSigninClicked()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Sign in with Google", color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Sign Up Link
            Row(modifier = Modifier.padding(bottom = 24.dp)) {
                Text("Don't have an account? ", color = Color.Gray)
                Text(
                    text = "Sign Up",
                    color = Color(0xFF215AF6),
                    modifier = Modifier.clickable {
                       onSignUpClicked()
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }



}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    AI_education_tutorTheme {
        LoginScreen(
            onLogin = {},
            onFogotpasClick = {},
            onSignUpClicked = {},
            onGoogleSigninClicked = {}
        )
    }
}