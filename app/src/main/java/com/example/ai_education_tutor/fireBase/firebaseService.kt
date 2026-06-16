package com.example.ai_education_tutor.fireBase

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.ai_education_tutor.dataClasses.Course
import com.example.ai_education_tutor.dataClasses.User
import com.example.ai_education_tutor.dataClasses.UserProgress
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firestore.v1.Value
import kotlinx.coroutines.tasks.await

object firebaseService {
    val dbfirebase = FirebaseFirestore.getInstance()
    val authdb = FirebaseAuth.getInstance()
    val userid = authdb.currentUser?.uid

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveCourse(course: Course, onSuccess: () -> Unit, onFailure: (Exception) -> Unit){

        val courseMap = mapOf(
            "courseId" to course.courseId,
            "title" to course.title,
            "description" to course.description,
            "thumbnailImageUrl" to course.thumbnailImageUrl,
            "category" to course.category,
            "difficulty" to course.difficulty,
            "totalLessons" to course.totalLessons,
            "estimatedDurationMinutes" to course.estimatedDurationMinutes,
            "aiGenerated" to course.aiGenerated,
            "tintColor" to course.tintColor,
            "bgColor" to course.bgColor,
            "modules" to course.modules.map { module ->
                mapOf(
                    "moduleId" to module.moduleId,
                    "courseId" to module.courseId,
                    "title" to module.title,
                    "description" to module.description,
                    "moduleOrder" to module.moduleOrder,
                    "tintColor" to module.tintColor,
                    "bgColor" to module.bgColor,
                    "lessons" to module.lessons.map { lesson ->
                        mapOf(
                            "lessonId" to lesson.lessonId,
                            "moduleId" to lesson.moduleId,
                            "title" to lesson.title,
                            "content" to lesson.content,
                            "lessonNumber" to lesson.lessonNumber,
                            "quizQuestions" to lesson.quizQuestions.map { quiz ->
                                mapOf(
                                    "questionId" to quiz.questionId,
                                    "questionText" to quiz.questionText,
                                    "options" to quiz.options,
                                    "correctAnswerIndex" to quiz.correctAnswerIndex
                                )
                            },
                            "resources" to lesson.resources
                        )
                    }

                )
            }

        )

        dbfirebase.collection("courses")
            .document(userid!!)
            .collection("courseList")
            .document(course.courseId)
            .set(course) // Ensure your Course class has default values like: data class Course(val id: String = "")
            .addOnSuccessListener {
                Log.d("dbsaveCourse", "course saved successfully")

                val progress = UserProgress(
                    userId = userid,
                    courseId = course.courseId,
                    progressPercentage = 0
                )

                addCourseProgress(progress)
                onSuccess()
            }
            .addOnFailureListener { error ->
                Log.e("dbsaveCourse", error.message.toString())
                onFailure(error)
            }


    }

    fun saveUserCredentials(credentials: User){

        dbfirebase.collection("users").document(credentials.userId).set(credentials).addOnSuccessListener {

        }.addOnFailureListener {

        }
    }
    fun getUserCredentials(userCredentials: (User) -> Unit){
        dbfirebase.collection("users").document(userid!!).get().addOnSuccessListener {
            val user = it.toObject(User::class.java)
            Log.d("User","${user}")
            if(user != null){
                userCredentials(user)
            }

        }
    }

    fun updateCredentials(key: String, value: Any, onSuccess: () -> Unit, onFailure: (Exception) -> Unit){

        dbfirebase.collection("users").document(userid!!).update(key,value).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {error ->
            onFailure(error)
        }

    }

    suspend fun changeUserEmail(currentPassword:String, newEmail: String):Boolean{

        val user = authdb.currentUser?: return false
        val currentEmail = authdb.currentUser?.email?: return false

        return try {

            val credentials = EmailAuthProvider.getCredential(currentEmail, currentPassword)
            user.reauthenticate(credentials).await()

            user.verifyBeforeUpdateEmail(newEmail).await()

            dbfirebase.collection("users")
                .document(userid!!)
                .update("email", newEmail)
                .await()



            true
        }catch (e: Exception){
            e.printStackTrace()
            false
        }

    }


    fun get_courses(courses:(List<Course>) -> Unit){
        dbfirebase.collection("courses")
            .document("$userid")
            .collection("courseList")
            .get()
            .addOnSuccessListener {
                var courseList = mutableListOf<Course>()

                for(i in it.documents){
                    val course = i.toObject(Course::class.java)
                    courseList.add(course!!)

                }

                courses(courseList)

                Log.d("get_courses", "couses captured succesfuly")
                Log.d("get_courses", "${courseList.forEach { it.bgColor }?: "null"}")



            }.addOnFailureListener { error ->
                Log.e("get_courses","error messge ${error.message}")
            }

    }
    fun addCourseProgress(progress: UserProgress){

        dbfirebase.collection("courseProgress")
            .document(userid!!)
            .collection("courses")
            .document(progress.courseId)
            .set(progress)
            .addOnSuccessListener {
            Log.d("progress", "progress saved succesfully")

        }.addOnFailureListener { error ->
            Log.d("progress","${error.message}")
        }

    }

    fun deletCourse(courseId: String, onSuccess: () -> Unit){

        dbfirebase.collection("courses")
            .document(userid!!)
            .collection("courseList")
            .document(courseId)
            .delete()
            .addOnSuccessListener {
                Log.d("deleteCourse","course deleted sucessfuly")
                deleteProgress(courseId)
                onSuccess()
            }
            .addOnFailureListener { error ->
                Log.e("deleteCourse","failed to delet course ${error.message}")
            }


    }

    fun deleteProgress(courseId: String){
        dbfirebase.collection("courseProgress")
            .document(userid!!)
            .collection("courses")
            .document(courseId)
            .delete()

    }


    fun getCourseProgress(progress: (List<UserProgress>) -> Unit) {

        val progressList = mutableListOf<UserProgress>()

        dbfirebase.collection("courseProgress")
            .document(userid!!)
            .collection("courses")
            .get()
            .addOnSuccessListener {
                for(i in it.documents){
                    val progress = i.toObject(UserProgress::class.java)
                    if(progress != null){
                        progressList.add(progress)
                    }
                }
                progress(progressList)
                Log.d("getCourseProgress","course progress caprtured succesfully")
            }
            .addOnFailureListener { error ->
                Log.e("getCourseProgress","${error.message}")

            }



    }











}