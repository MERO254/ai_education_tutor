package com.example.ai_education_tutor.dataClasses


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class User(
    val userId: String = "",
    val email: String = "",
    val fullName: String = "",
    val isProMember: Boolean = false,
    val profilePictureUrl: String? = null,
    val learningPreferences: List<String> = emptyList(),
    val createdAt: String = LocalDateTime.now().toString(),
    val lastLogin: String = LocalDateTime.now().toString()
)

@RequiresApi(Build.VERSION_CODES.O)
data class UserProgress(
    val userId: String = "",
    val courseId: String = "",
    val progressPercentage: Int = 0,
    val completed: Boolean = false,
    val lastAccessed: String = LocalDateTime.now().toString(),
    val studyHours: Float = 0.0f,
    val badgesEarned: List<String> = emptyList(),
    val milestones: List<String> = emptyList()
)

data class Course(
    val courseId: String = "",
    val title: String = "",
    val description: String = "",
    val thumbnailImageUrl: String? = null,
    val category: String = "",
    val difficulty: String = "",
    val totalLessons: Int = 0,
    val estimatedDurationMinutes: Int = 0,
    val aiGenerated: Boolean = false,
    val tintColor: String = "",
    val bgColor: String = "",
    val status : String = statusValue.ACTIVE.toString(),
    val modules: List<Module> = emptyList() // ➔ Points cleanly to the mid-tier layer
){
    enum class statusValue{
        IN_PROGRESS,
        COMPLETE,
        ACTIVE
    }
}

// 2. MID TIER: Module acts as a chapter containing sub-lessons
data class Module(
    val moduleId: String = "",
    val courseId: String = "",           // Back-reference link to parent Course
    val title: String = "",
    val description: String = "",
    val moduleOrder: Int = 0,
    val tintColor: String = "",
    val bgColor: String = "",
    val lessons: List<Lesson> = emptyList() // ➔ Points cleanly to the leaf-node layer
)

// 3. BASE TIER: Lesson holds core educational markdown data and quizzes
data class Lesson(
    val lessonId: String = "",
    val moduleId: String = "",           // Fixed: Relates back to parent Module instead of Course
    val title: String = "",
    val content: String = "",            // Markdown or HTML content text string
    val lessonNumber: Int = 0,           // Sequence order of reading inside the parent module
    val quizQuestions: List<QuizQuestion> = emptyList(), // Cleaned: Replaced generic Map with a typed class
    val resources: List<String> = emptyList()
)

// 4. EXTRA HELPING CLASS: Strongly typed alternative to List<Map<String, Any>>
data class QuizQuestion(
    val questionId: String = "",
    val questionText: String = "",
    val options: List<String> = emptyList(),
    val correctAnswerIndex: Int = 0
)

@RequiresApi(Build.VERSION_CODES.O)
data class AIChatMessage(
    val messageId: String = "",
    val userId: String = "",
    val sender: String = "", // "user" or "ai"
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val textContent: String = "",
    val suggestedActions: List<String> = emptyList() // e.g., ["View Resource", "Generate More Topics"]
)

data class AITopic(
    val topicId: String = "",
    val courseId: String? = null, // If part of an existing course
    val title: String = "",
    val description: String = "",
    val difficulty: String = "",
    val estimatedDurationMinutes: Int = 0,
    val generatedByAi: Boolean = true
)
