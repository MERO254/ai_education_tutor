package com.example.ai_education_tutor.screens


import ProgressDashboardScreen
import com.example.ai_education_tutor.R
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
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
import com.example.ai_education_tutor.ui.theme.AI_education_tutorTheme

// --- Data Models ---
data class Lesson(
    val id: Int,
    val title: String,
    val content: String,
    val imageRes: Int,
    val quizQuestion: String,
    val quizOptions: List<String>,
    val correctAnswerIndex: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreen(onBack: () -> Unit) {
    // Mock Data List
    val courseLessons = listOf(
        Lesson(
            1, "What is Machine Learning?",
            "Machine Learning is a branch of artificial intelligence that enables systems to learn from data and improve over time without being explicitly programmed.",
            R.drawable.img_robot_tutor, // Replace with your actual drawable
            "Which of the following best describes Machine Learning?",
            listOf("Computers following pre-defined rules", "Computers learning from data and improving over time", "Manual programming of every decision", "A type of computer hardware"),
            1
        ),
        Lesson(
            2, "Supervised vs Unsupervised",
            "Supervised learning uses labeled data to train algorithms, whereas unsupervised learning finds hidden patterns in unlabeled data.",
            R.drawable.img_robot_tutor,
            "What type of learning uses labeled data?",
            listOf("Unsupervised Learning", "Reinforcement Learning", "Supervised Learning", "Deep Learning"),
            2
        )
    )

    // State management for navigation
    var currentLessonIndex by remember { mutableStateOf(0) }
    val currentLesson = courseLessons[currentLessonIndex]
    val progress = (currentLessonIndex + 1).toFloat() / courseLessons.size

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lesson ${currentLesson.id} of ${courseLessons.size}", fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null) }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(painter = painterResource(id = R.drawable.ic_bookmark), contentDescription = null) }
                }
            )
        },
        bottomBar = {
            LessonNavigationButtons(
                isFirst = currentLessonIndex == 0,
                isLast = currentLessonIndex == courseLessons.size - 1,
                onPrevious = { currentLessonIndex-- },
                onNext = { currentLessonIndex++ }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                color = Color(0xFF6366F1),
                trackColor = Color(0xFFF1F5F9)
            )
            Text(
                "${(progress * 100).toInt()}% Complete",
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp),
                fontSize = 12.sp, color = Color(0xFF6366F1), fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Title and Content
            Text(currentLesson.title, fontSize = 28.sp, fontWeight = FontWeight.Bold, lineHeight = 34.sp)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    currentLesson.content,
                    modifier = Modifier.weight(1f).padding(top = 16.dp),
                    fontSize = 15.sp, color = Color.Gray, lineHeight = 22.sp
                )
                Image(
                    painter = painterResource(id = currentLesson.imageRes),
                    contentDescription = null,
                    modifier = Modifier.size(140.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Quiz Section
            QuickQuizCard(currentLesson)
        }
    }
}

@Composable
fun QuickQuizCard(lesson: Lesson) {
    var selectedOption by remember(lesson.id) { mutableStateOf(-1) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF8FAFF),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color(0xFFEDF2F7))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(color = Color(0xFF6366F1), shape = CircleShape, modifier = Modifier.size(32.dp)) {
                    Text("?", color = Color.White, modifier = Modifier.wrapContentSize(), fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("Quick Quiz", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text("1 of 1", color = Color.Gray, fontSize = 12.sp)
            }

            Text(lesson.quizQuestion, modifier = Modifier.padding(vertical = 16.dp), fontSize = 14.sp, fontWeight = FontWeight.Medium)

            lesson.quizOptions.forEachIndexed { index, option ->
                val isSelected = selectedOption == index
                val borderColor = if (isSelected) Color(0xFF6366F1) else Color(0xFFEDF2F7)
                val bgColor = if (isSelected) Color(0xFFEEF2FF) else Color.White

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .selectable(selected = isSelected, onClick = { selectedOption = index }),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, borderColor),
                    color = bgColor
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = isSelected, onClick = null, colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF6366F1)))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(option, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun LessonNavigationButtons(isFirst: Boolean, isLast: Boolean, onPrevious: () -> Unit, onNext: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(20.dp).navigationBarsPadding(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(
            onClick = onPrevious,
            modifier = Modifier.weight(1f).height(56.dp),
            enabled = !isFirst,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = null)
            Text("Previous", fontWeight = FontWeight.Bold)
        }

        Button(
            onClick = onNext,
            modifier = Modifier.weight(1f).height(56.dp),
            enabled = !isLast,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1))
        ) {
            Text("Next", fontWeight = FontWeight.Bold)
            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.padding(start = 8.dp))
        }
    }
}



@Preview(showBackground = true)
@Composable
fun lessonScreenPreview() {
    AI_education_tutorTheme {
        LessonScreen{
            
        }
    }
}