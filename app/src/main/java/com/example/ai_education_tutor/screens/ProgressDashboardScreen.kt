import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai_education_tutor.R // Ensure this matches your package
import com.example.ai_education_tutor.screens.AITopicGeneratorScreen
import com.example.ai_education_tutor.ui.theme.AI_education_tutorTheme

@Composable
fun ProgressDashboardScreen() {
    Scaffold(
        topBar = { ProgressTopBar() }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8FAFF)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. Overall Progress Header Card
            item { OverallProgressCard() }

            // 2. Weekly Study Hours Bar Chart
            item { WeeklyStudyHoursCard() }

            // 3. Badges Earned Section
            item { BadgesEarnedSection() }

            // 4. Milestones Section
            item { MilestonesSection() }
        }
    }
}

@Composable
fun OverallProgressCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(listOf(Color(0xFF4E73DF), Color(0xFF224ABE)))
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Overall Progress", color = Color.White.copy(alpha = 0.8f))
                    Text("72%", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold)
                    Text("Complete", color = Color.White.copy(alpha = 0.8f))
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "↑ 12% from last month",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }

                // Custom Circular Progress
                Box(contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.size(110.dp)) {
                        drawArc(
                            color = Color.White.copy(alpha = 0.2f),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = Color(0xFF00F2FE),
                            startAngle = -90f,
                            sweepAngle = 260f,
                            useCenter = false,
                            style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(painter = painterResource(id = R.drawable.ic_target), contentDescription = null, tint = Color.White)
                        Text("72%", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("of your goal", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun WeeklyStudyHoursCard() {
    val data = listOf(2.5f, 3.0f, 4.5f, 2.0f, 6.0f, 7.0f, 1.5f)
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Weekly Study Hours", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("This Week: 18.5 hrs", color = Color(0xFF4E73DF), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth().height(140.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                data.forEachIndexed { index, value ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(value.toString(), fontSize = 10.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .fillMaxHeight(value / 8f) // Scaled to 8hr max
                                .background(Color(0xFF4E73DF), RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(days[index], fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun BadgesEarnedSection() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Badges Earned", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            TextButton(onClick = {}) {
                Text("View all", color = Color(0xFF4E73DF), fontSize = 14.sp)
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            val badges = listOf(
                BadgeData("First Steps",Color(0xFFE8F5E9), Color(0xFF4CAF50)),
                BadgeData("Consistent",Color(0xFFF3F1FF), Color(0xFF9181F4)),
                BadgeData("Time Master",Color(0xFFDDE7FF), Color(0xFF215AF6)),
                BadgeData("Goal Getter",Color(0xFFFFF4E5), Color(0xFFFF9800)),
                BadgeData("Knowledge",Color(0xFFFFEBEE), Color(0xFFF44336))
            )

            items(badges) { badge ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        modifier = Modifier.size(60.dp),
                        shape = CircleShape,
                        color = badge.bgColor,
                        shadowElevation = 1.dp
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_module),
                            contentDescription = null,
                            tint = badge.iconColor,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(badge.name, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                }
            }
        }
    }
}

@Composable
fun MilestonesSection() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Milestones", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            TextButton(onClick = {}) {
                Text("View all", color = Color(0xFF4E73DF), fontSize = 14.sp)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                MilestoneItem(
                    title = "Completed 5 Courses",
                    description = "Great job! You've completed 5 courses.",
                    status = "Completed",
                    date = "May 10, 2024",
                    icon = R.drawable.ic_circlecheck,
                    iconColor = Color(0xFF4CAF50),
                    isLast = false
                )
                MilestoneItem(
                    title = "Study Streak: 14 Days",
                    description = "Keep it up! You've studied for 14 days in a row.",
                    status = "In Progress",
                    date = "70%",
                    icon = R.drawable.ic_stars,
                    iconColor = Color(0xFF215AF6),
                    isLast = false
                )
                MilestoneItem(
                    title = "100 Study Hours",
                    description = "You're on your way to becoming unstoppable!",
                    status = "Upcoming",
                    date = "18.5 / 100 hrs",
                    icon = R.drawable.ic_radiobuttonunchecked,
                    iconColor = Color.LightGray,
                    isLast = true
                )
            }
        }
    }
}

@Composable
fun MilestoneItem(
    title: String,
    description: String,
    status: String,
    date: String,
    icon: Int,
    iconColor: Color,
    isLast: Boolean
) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(painterResource(icon), contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(Color(0xFFF0F0F0))
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.padding(bottom = 24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)

                Surface(
                    color = when(status) {
                        "Completed" -> Color(0xFFE8F5E9)
                        "In Progress" -> Color(0xFFDDE7FF)
                        else -> Color(0xFFF5F5F5)
                    },
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when(status) {
                            "Completed" -> Color(0xFF4CAF50)
                            "In Progress" -> Color(0xFF215AF6)
                            else -> Color.Gray
                        }
                    )
                }
            }
            Text(description, fontSize = 12.sp, color = Color.Gray, lineHeight = 16.sp)
            Text(date, fontSize = 11.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
fun ProgressTopBar() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {}) { Icon(Icons.Default.Menu, null) }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Progress Dashboard", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Keep learning, keep growing! 🚀", color = Color.Gray, fontSize = 12.sp)
        }
        Image(
            painter = painterResource(id = R.drawable.ic_profile_avatar), // Replace with your avatar
            contentDescription = null,
            modifier = Modifier.size(40.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

data class BadgeData(val name: String, val bgColor: Color, val iconColor: Color)



@Preview(showBackground = true)
@Composable
fun ProgressDashboeardPreview() {
    AI_education_tutorTheme {
        ProgressDashboardScreen()
    }
}