import com.example.ai_education_tutor.dataClasses.UserProgress
import com.example.ai_education_tutor.fireBase.firebaseService

object progresTracking {

    private val _progressList = kotlinx.coroutines.flow.MutableStateFlow<List<UserProgress>>(emptyList())
    val progressList = _progressList

    init {
        getprogress()
    }

    private fun getprogress() {
        firebaseService.getCourseProgress { list ->
            _progressList.value = list
        }
    }

    fun finishedCourses(): Int {
        return _progressList.value.count { it.completed == true }
    }

    fun learningHours(): Double {
        return _progressList.value.sumOf { it.studyHours.toDouble() }
    }

    fun certificates(): Int {
        return _progressList.value.sumOf { it.badgesEarned.size }
    }
}