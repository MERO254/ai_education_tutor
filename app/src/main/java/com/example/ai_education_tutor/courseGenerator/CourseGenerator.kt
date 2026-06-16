package com.example.ai_education_tutor.courseGenerator

import android.util.Log
import com.example.ai_education_tutor.dataClasses.Course
import com.example.ai_education_tutor.dataClasses.Lesson
import com.example.ai_education_tutor.dataClasses.Module
import com.example.ai_education_tutor.dataClasses.QuizQuestion
import com.google.ai.client.generativeai.GenerativeModel
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class CourseGenerator(val unit: String) {

    suspend fun generateCompleteCourse(): Course {
        return try {
            val model = GenerativeModel(
                modelName = "gemini-2.5-flash-lite",
                apiKey = "AIzaSyDMpZqG_iMVGV4Bdq7FzaduqcvKhqMHNig"
            )

            val prompt = """
You are an expert AI education tutor. Create a structured, comprehensive, and complete introductory course model for the unit study topic: "$unit".

## STRUCTURE RULES — follow exactly, no exceptions
- Total modules: exactly 12
- Lessons per module: exactly 3
- Quiz questions per lesson: exactly 3
- Modules 1–4: introductory level (no prior knowledge assumed)
- Modules 5–8: intermediate level (builds on earlier modules)
- Modules 9–12: advanced level (synthesis and application)

## CONTENT RULES — per lesson
  1. An opening explanation of the concept
  2. A worked example or real-world analogy
  3. A key takeaway summary
- "quizQuestions": 3 questions testing DIFFERENT aspects:
  - Q1: definition or recall
  - Q2: application or example recognition  
  - Q3: reasoning or 'why/how' question
- "correctAnswerIndex": MUST vary across questions — do not default to 0. Distribute answers across indices 0, 1, 2, and 3.

## OUTPUT RULES
- Return ONLY raw valid JSON — no markdown fences, no preamble, no trailing text
- Every string value must be properly escaped
- The JSON must be parseable by Android's org.json.JSONObject

{
    "title": "$unit",
    "description": "A comprehensive 2-3 sentence summary detailing the syllabus scope of this course.",
    "category": "General Education",
    "difficulty": "Beginner",
    "estimatedDurationMinutes": 120,
    "modules": [
        {
            "title": "Module Chapter Title",
            "description": "Brief summary of what this module covers.",
            "lessons": [
                {
                    "title": "Lesson Title Name",
                    "content": "# Core Concepts\n\nThis is the markdown string layout explaining the topic depth safely...",
                    "quizQuestions": [
                        {
                            "questionText": "What is the primary factor behind this system design?",
                            "options": ["Option A text description", "Option B text description", "Option C text description", "Option D text description"],
                            "correctAnswerIndex": 0
                        }
                    ]
                }
            ]
        }
    ]
}
""".trimIndent()

            val response = model.generateContent(prompt)
            val text = response.text ?: ""
            val cleanedText = cleanJsonResponse(text)

            Log.d("CourseGenerator", "Response received, length: ${cleanedText.length}")
            parseCourse(cleanedText)

        } catch (e: Exception) {
            Log.e("CourseGenerator", "Generation breakdown pipeline exception: ${e.message}")
            e.printStackTrace()
            Course(
                title = unit,
                tintColor = "#6C5CE7",
                bgColor = "#F3F1FF",
                status = "Active" // Safe backup string assignment
            )
        }
    }

    private fun parseCourse(jsonString: String): Course {
        val json = JSONObject(jsonString)
        val courseId = UUID.randomUUID().toString()

        // 1. Course gets a safe base color definition
        val (courseTint, courseBg) = getThemeColorPairingByIndex(0)

        val jsonModules = json.optJSONArray("modules") ?: JSONArray()
        val finalModulesList = mutableListOf<Module>()
        var globalLessonCounter = 0
        var totalLessonsInCourse = 0

        for (i in 0 until jsonModules.length()) {
            val moduleObj = jsonModules.getJSONObject(i)
            val moduleId = UUID.randomUUID().toString()

            // FIX: Modules now cycle through colors based on index position so they don't look identical
            val (moduleTint, moduleBg) = getThemeColorPairingByIndex(i)

            val jsonLessons = moduleObj.optJSONArray("lessons") ?: JSONArray()
            val finalLessonsList = mutableListOf<Lesson>()

            for (j in 0 until jsonLessons.length()) {
                val lessonObj = jsonLessons.getJSONObject(j)
                val lessonId = UUID.randomUUID().toString()
                globalLessonCounter++
                totalLessonsInCourse++

                val jsonQuizzes = lessonObj.optJSONArray("quizQuestions") ?: JSONArray()
                val finalQuizList = mutableListOf<QuizQuestion>()

                for (k in 0 until jsonQuizzes.length()) {
                    val quizObj = jsonQuizzes.getJSONObject(k)
                    val jsonOptions = quizObj.optJSONArray("options") ?: JSONArray()
                    val optionsList = mutableListOf<String>()

                    for (m in 0 until jsonOptions.length()) {
                        optionsList.add(jsonOptions.getString(m))
                    }

                    finalQuizList.add(
                        QuizQuestion(
                            questionId = UUID.randomUUID().toString(),
                            questionText = quizObj.optString("questionText", ""),
                            options = optionsList,
                            correctAnswerIndex = quizObj.optInt("correctAnswerIndex", 0)
                        )
                    )
                }

                finalLessonsList.add(
                    Lesson(
                        lessonId = lessonId,
                        moduleId = moduleId,
                        title = lessonObj.optString("title", "Untitled Lesson"),
                        content = lessonObj.optString("content", ""),
                        lessonNumber = globalLessonCounter,
                        quizQuestions = finalQuizList,
                        resources = emptyList()
                    )
                )
            }

            finalModulesList.add(
                Module(
                    moduleId = moduleId,
                    courseId = courseId,
                    title = moduleObj.optString("title", "Untitled Module"),
                    description = moduleObj.optString("description", ""),
                    moduleOrder = i + 1,
                    tintColor = moduleTint,
                    bgColor = moduleBg,
                    lessons = finalLessonsList
                )
            )
        }

        // Make sure this matches your data class enum configuration string structure
        val finalStatus = Course.statusValue.ACTIVE.toString()

        return Course(
            courseId = courseId,
            title = json.optString("title", unit),
            description = json.optString("description", ""),
            category = json.optString("category", "General"),
            difficulty = json.optString("difficulty", "Beginner"),
            totalLessons = totalLessonsInCourse,
            estimatedDurationMinutes = json.optInt("estimatedDurationMinutes", 60),
            aiGenerated = true,
            tintColor = courseTint,
            bgColor = courseBg,
            modules = finalModulesList,
            status = finalStatus
        )
    }

    private fun cleanJsonResponse(response: String): String {
        var cleaned = response.trim()
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7)
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3)
        }
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length - 3)
        }
        cleaned = cleaned.trim()

        val jsonStart = cleaned.indexOf('{')
        val jsonEnd = cleaned.lastIndexOf('}')
        if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
            cleaned = cleaned.substring(jsonStart, jsonEnd + 1)
        }
        return cleaned
    }

    /**
     * IMPROVEMENT: Replaced random assignment with modulo index lookup.
     * This ensures colors roll over sequentially across your 12 modules cleanly.
     */
    private fun getThemeColorPairingByIndex(index: Int): Pair<String, String> {
        val pairings = listOf(
            Pair("#6C5CE7", "#F3F1FF"), // Elegant Purple
            Pair("#215AF6", "#DDE7FF"), // Accent Blue
            Pair("#4CAF50", "#E8F5E9"), // Eco Green
            Pair("#FF9800", "#FFF4E5")  // Warm Orange
        )
        return pairings[index % pairings.size]
    }
}