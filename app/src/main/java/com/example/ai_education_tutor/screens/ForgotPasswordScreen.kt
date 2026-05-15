package com.example.ai_education_tutor.screens

import com.example.ai_education_tutor.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai_education_tutor.ui.theme.AI_education_tutorTheme
import java.nio.file.WatchEvent

@Composable
fun ForgotPasswordScreen(onBackClick: () -> Unit){

    var email by remember { mutableStateOf("") }


    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),

        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Spacer(modifier = Modifier.height(60.dp))

        //robot illustaration

        Image(
            painter = painterResource(id = R.drawable.img_robot_tutor),
            contentDescription = null,
            modifier = Modifier.size(180.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        //title and subtitle
        Text(
            text = "Reset Password",
            style  = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF000814)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Enter your Email to recieve reset link",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        //email input

        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            placeholder = {Text("Email", color = Color.LightGray)},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = Color.Gray
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = Color(0xFF215AF6),
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        //send link button

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),

            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF215AF6),
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)

        ) {

            Text(
                text = "Send Reset Link",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Back To Login",
            color = Color(0xFF215AF6),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .clickable {  }
        )

    }

}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordPreview() {
    AI_education_tutorTheme {
        ForgotPasswordScreen {  }
    }
}