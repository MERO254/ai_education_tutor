package com.example.ai_education_tutor.screens



import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai_education_tutor.Activities.AITopicGenerator_Activity
import com.example.ai_education_tutor.R
import com.example.ai_education_tutor.ui.theme.AI_education_tutorTheme

@Composable
fun HomeScreen() {
    // 1. Wrap the entire layout in a Box to allow independent floating elements

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // Your existing scrollable main feed
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            HeaderSection()

            Spacer(modifier = Modifier.height(20.dp))

            WelcomeSection("Elvis")

            Spacer(modifier = Modifier.height(12.dp))

            StreakCard()

            Spacer(modifier = Modifier.height(22.dp))

            ContinueLearningSection()

            Spacer(modifier = Modifier.height(22.dp))

            RecommendedTopicSection()

            // 2. Extra bottom spacer so scrolling content doesn't hide behind the floating button
            Spacer(modifier = Modifier.height(80.dp))
        }

        // 3. Add the Floating Button anchored to the bottom right corner
        ExtendedFloatingActionButton(
            onClick = {
                /* Handle your generate course action here */
                val intent = Intent(context, AITopicGenerator_Activity::class.java)
                context.startActivity(intent)

            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 16.dp), // Safe margin styling
            containerColor = Color(0xFF215AF6),       // Matches your daily streak blue theme accent
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_stars), // Adds a sleek AI/Sparkle icon context
                    contentDescription = null,
                    tint = Color.White
                )
            },
            text = {
                Text(
                    text = "Generate Course",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        )
    }
}

// --- Keeping your design subcomponents completely unchanged underneath ---

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Menu,
            contentDescription = "Menu",
            modifier = Modifier.size(24.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color.Unspecified,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_profile_avatar),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
fun WelcomeSection(userName: String) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Text(
                text = "Good morning, 👋",
                color = Color.Gray,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = userName,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Let's keep up your learning momentum.",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ic_ai_logo),
            contentDescription = null,
            modifier = Modifier
                .size(82.dp)
                .align(Alignment.CenterEnd)
        )
    }
}

@Composable
fun StreakCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color(0xFFF4F7FD),
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(13.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFDDE7FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Face,
                        contentDescription = null,
                        tint = Color(0xFF215AF6),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        "Daily Streak",
                        color = Color(0xFF215AF6),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )

                    Text(
                        "7 Days",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                "Great job! Keep it going 🔥",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun ContinueLearningSection() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Continue Learning",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                fontSize = 17.sp
            )

            Text(
                "View all >",
                color = Color(0xFF215AF6),
                fontSize = 12.sp,
                modifier = Modifier.clickable { }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            elevation = 2.dp,
            backgroundColor = Color.White
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            color = Color(0xFF9181F4),
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_code),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Python Basics",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    LinearProgressIndicator(
                        progress = 0.68f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .height(5.dp)
                            .clip(CircleShape),
                        color = Color(0xFF9181F4),
                        trackColor = Color(0xFFEEEEEE)
                    )

                    Text(
                        "68% • 25 min left",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            Color(0xFFF4F7FD),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color(0xFF9181F4),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendedTopicSection() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "AI Recommended Topics",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                fontSize = 17.sp
            )

            Text(
                "See all",
                color = Color(0xFF215AF6),
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(4) {
                TopicCard()
            }
        }
    }
}

@Composable
fun TopicCard() {
    Card(
        modifier = Modifier.width(135.dp),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = Color(0xFFF4F7FD),
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(13.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_machine_learning),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                "Machine Learning 101",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                "Beginner",
                color = Color(0xFF215AF6),
                fontSize = 11.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AI_education_tutorTheme {
        HomeScreen()
    }
}