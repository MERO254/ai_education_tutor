package com.example.ai_education_tutor.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ai_education_tutor.R
import com.example.ai_education_tutor.navigation.AppNavigation
import com.example.ai_education_tutor.ui.theme.AI_education_tutorTheme
import kotlinx.coroutines.flow.WhileSubscribed
import java.nio.file.WatchEvent

@Composable
fun HomeScreen(){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        HeaderSection()

        Spacer(modifier = Modifier.height(24.dp))

        WelcomeSection("Elvis")

        Spacer(modifier = Modifier.height(10.dp))


        StreakCard()

        Spacer(modifier = Modifier.height(24.dp))


        ContinueLearningSection()


        Spacer(modifier = Modifier.height(24.dp))


        RecommendedTopicSection()


        Spacer(modifier = Modifier.height(24.dp))







    }

}

@Composable
fun HeaderSection(){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(Icons.Default.Menu, contentDescription = "Menu", modifier = Modifier.size(28.dp))
        Row(verticalAlignment = Alignment.CenterVertically){
            Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.Unspecified)
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_profile_avatar),
                contentDescription = "Profile",
                modifier = Modifier.size(40.dp).clip(CircleShape)

            )
        }

    }
}

@Composable
fun WelcomeSection(userName: String){
    Box(modifier = Modifier.fillMaxWidth()){
        Column {
            Text(text = "Good morning, 👋", color = Color.Gray, style = MaterialTheme.typography.bodyLarge)
            Text(text = userName, style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold))
            Text(text = "Let's keep up your learning momentum.", color = Color.Gray)
        }

        Image(
            painter = painterResource(id = R.drawable.ic_ai_logo),
            contentDescription = null,
            modifier = Modifier.size(120.dp).align(Alignment.CenterEnd)
        )

    }
}

@Composable
fun StreakCard(){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = Color(0xFFF4F7FD),
        elevation = 0.dp
    ){

        Column(
            modifier = Modifier.padding(16.dp)
        ){

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier.size(50.dp).background(Color(0xFFDDE7FF),shape = RoundedCornerShape(30.dp)).clip(RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ){

                    Icon(Icons.Default.Face, contentDescription = null, tint = Color(0xFF215AF6))

                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text("Daily Streak", color = Color(0xFF215AF6), fontWeight = FontWeight.SemiBold)
                    Text("7 Days", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
                }

            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("Great job! Keep it going 🔥", color = Color.Gray)

        }

    }
}

@Composable
fun ContinueLearningSection(){
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Text("Continue Learning", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Text("View all >", color = Color(0xFF215AF6), modifier = Modifier.clickable{ })
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = 4.dp,
            backgroundColor = Color.White
        ) {

            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            color = Color(0xFF9181F4), // This makes the white icon visible!
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_code),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text("Python Basics", fontWeight = FontWeight.Bold)
                    LinearProgressIndicator(
                        progress = 0.68f,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(6.dp).clip(CircleShape),
                        color = Color(0xFF9181F4),
                        trackColor = Color(0xFFEEEEEE)
                    )
                    Text("68% • 25 min left", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }

                IconButton(onClick = {}, modifier = Modifier.background(Color(0xFFF4F7FD), CircleShape)) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFF9181F4))
                }

            }

        }
    }
}

@Composable
fun RecommendedTopicSection(){
    Column{
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Text("AI Recommended Topics", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Text("See all", color = Color(0xFF215AF6))
        }

        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(4) {
                TopicCard()
            }
        }
    }
}

@Composable
fun TopicCard(){
    Card(
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color(0xFFF4F7FD),
        elevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(painter = painterResource(id = R.drawable.ic_machine_learning), contentDescription = null, modifier = Modifier.size(40.dp), tint = Color.Unspecified)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Machine Learning 101", fontWeight = FontWeight.Bold, maxLines = 2)
            Text("Beginner", color = Color(0xFF215AF6), style = MaterialTheme.typography.bodySmall)
        }
    }

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AI_education_tutorTheme {
        HomeScreen()
    }
}
