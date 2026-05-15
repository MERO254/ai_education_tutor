package com.example.ai_education_tutor.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Colors
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai_education_tutor.ui.theme.AI_education_tutorTheme
import java.nio.file.WatchEvent

@Composable

fun RegisterScreen(onBackClick:() -> Unit, onRegisterClick: () -> Unit){

    var fullName by remember { mutableStateOf("") }
    var email by remember {mutableStateOf("")}
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isAgreed by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),

        horizontalAlignment = Alignment.CenterHorizontally


    ) {

        // top bar

        Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)){

            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }

        }

        //robot illustration

        Image(
            painter = painterResource(id = R.drawable.ic_tutor_robot2),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        //title section

        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
        )

        Text(
            text = "join AI Tutor and Start your learning journey",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        //input fields
        RegisterTextField(value = fullName, onValueChange = {fullName = it}, label = "Full Name", icon = Icons.Default.Person)
        Spacer(modifier = Modifier.height(16.dp))

        RegisterTextField(value = email, onValueChange = {email = it}, label = "Email", icon = Icons.Default.Email)
        Spacer(modifier = Modifier.height(16.dp))

        RegisterTextField(value = password, onValueChange = {password = it}, label = "Password", icon = Icons.Default.Lock, isPassword = true)
        Spacer(modifier = Modifier.height(16.dp))

        RegisterTextField(value = confirmPassword, onValueChange = {confirmPassword= it}, label = "Confirm Password", icon = Icons.Default.Lock, isPassword = true)
        Spacer(modifier = Modifier.height(16.dp))


        //terms and conditions
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isAgreed,
                onCheckedChange = {isAgreed = it},
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF215AF6))
            )

            Text(text = "I agree to the", style = MaterialTheme.typography.bodySmall)
            Text(
                text = "Terms & Conditions",
                color = Color(0xFF215AF6),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.clickable{
                    //show terms
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        //sign up button

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF215AF6))
        ) {
            Text("Sign Up", fontSize = 18.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        //footer
        Row(modifier = Modifier.padding(bottom = 24.dp)) {

            Text("Already have an account?", color = Color.Gray)
            Text(
                text = "Log In",
                color = Color(0xFF215AF6),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable{onRegisterClick}
            )

        }

    }
}

@Composable
fun RegisterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    isPassword: Boolean = false
){

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {Icon(imageVector = icon, contentDescription = null, tint = Color.Gray)},
        trailingIcon = {if(isPassword) Icon(painter = painterResource(id = R.drawable.ic_eye_off), contentDescription = null)},
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        visualTransformation = if(isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedBorderColor = Color(0xFF215AF6)
        )


    )

}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    AI_education_tutorTheme {
        RegisterScreen(
            onRegisterClick = {},
            onBackClick = {}
        )
    }
}