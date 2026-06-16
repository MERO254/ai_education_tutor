package com.example.ai_education_tutor.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai_education_tutor.R
import com.example.ai_education_tutor.ui.theme.AI_education_tutorTheme

// Data class for Chat Messages
data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val time: String,
    val showResourceButton: Boolean = false
)

@Composable
fun AI_TutorScreen(onBackClick: () -> Unit) {

    val messages = remember {
        mutableStateListOf(
            ChatMessage("Can you explain photosynthesis in simple terms?", true, "9:40 AM"),
            ChatMessage(
                "Absolutely! 🌱\nPhotosynthesis is the process used by plants to make their own food. They use sunlight, water, and carbon dioxide to produce glucose (food) and oxygen.",
                false,
                "9:41 AM",
                showResourceButton = true
            ),
            ChatMessage("That makes so much sense! Thanks 😊", true, "9:42 AM")
        )
    }

    // Root Container replacing Scaffold completely
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FC))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // 1. TOP HEADER LAYER (Custom TopAppBar)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back Action
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }

                    // Centered Profile Header Info
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_robot_tutor),
                            contentDescription = "AI Avatar",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "AI Tutor",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.ic_stars),
                                contentDescription = null,
                                tint = Color(0xFF6C5CE7),
                                modifier = Modifier.size(12.dp)
                            )
                        }

                        Text(
                            text = "Always here to help you learn",
                            color = Color.Gray,
                            fontSize = 9.sp
                        )
                    }

                    // Options Action
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = Color.Gray
                        )
                    }
                }
            }

            // 2. MIDDLE CHAT STREAM LAYER
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFF7F8FC))
            ) {
                // Date Stamp Badge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        color = Color(0xFFE9ECF3),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Today",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Scrollable Feed List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(messages) { message ->
                        ChatBubble(message)
                    }
                }
            }

            // 3. BOTTOM CHAT INPUT FIELD LAYER
            ChatInputArea()
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val bubbleColor = if (message.isUser) Color(0xFF215AF6) else Color(0xFFF0EEFF)
    val textColor = if (message.isUser) Color.White else Color.Black
    val alignment = if (message.isUser) Alignment.End else Alignment.Start

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Row(verticalAlignment = Alignment.Bottom) {

            // Show Mini Bot Avatar only on incoming messages
            if (!message.isUser) {
                Image(
                    painter = painterResource(id = R.drawable.img_robot_tutor),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }

            Surface(
                color = bubbleColor,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.widthIn(max = 260.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = message.text,
                        color = textColor,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    // Resource Button Injection (if applicable)
                    if (message.showResourceButton) {
                        Spacer(modifier = Modifier.height(8.dp))

                        // Enhanced Card/Box view for the resource button with better rounded corners
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Transparent
                            ),
                            border = BorderStroke(1.5.dp, Color(0xFF6C5CE7))
                        ) {
                            OutlinedButton(
                                onClick = {},
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.White.copy(alpha = 0.9f)
                                ),
                                border = BorderStroke(0.dp, Color.Transparent),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_menubook),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFF6C5CE7)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "View Resource",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF6C5CE7)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = message.time,
                        color = if (message.isUser) Color.White.copy(alpha = 0.7f) else Color.Gray,
                        fontSize = 9.sp,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatInputArea() {
    var text by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 6.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .navigationBarsPadding()
                .imePadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Attachment action
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_attacjmentfile),
                    contentDescription = "Attach file",
                    tint = Color(0xFF215AF6)
                )
            }

            // Chat input field text box
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Type a message...", fontSize = 14.sp) },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
            )

            // Voice command/Send action
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_mic),
                    contentDescription = "Voice Input",
                    tint = Color(0xFF215AF6),
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AI_TutorPreview() {
    AI_education_tutorTheme {
        AI_TutorScreen {}
    }
}