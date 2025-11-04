package com.example.tranphambachcat_se184684.util

import android.util.Log
import com.example.tranphambachcat_se184684.data.local.CourseDao
import com.example.tranphambachcat_se184684.data.local.FavoriteDao
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Debug helper để log database content ra Logcat
 * CHỈ DÙNG ĐỂ DEBUG - XÓA TRONG PRODUCTION
 */
class DatabaseDebug @Inject constructor(
    private val courseDao: CourseDao,
    private val favoriteDao: FavoriteDao
) {
    suspend fun logAllCourses() {
        val courses = courseDao.getAllCourses().first()
        Log.d("DB_DEBUG", "═══════════════════════════════════")
        Log.d("DB_DEBUG", "COURSES TABLE (${courses.size} rows)")
        Log.d("DB_DEBUG", "═══════════════════════════════════")
        courses.forEach { course ->
            Log.d("DB_DEBUG", """
                ID: ${course.id}
                Title: ${course.title}
                Level: ${course.level}
                IsFavorite: ${course.isFavorite} ⭐
                Price: ${course.price}
                Author: ${course.authorName}
                ---
            """.trimIndent())
        }
    }

    suspend fun logAllFavorites() {
        val favorites = favoriteDao.getAllFavorites().first()
        Log.d("DB_DEBUG", "═══════════════════════════════════")
        Log.d("DB_DEBUG", "FAVORITES TABLE (${favorites.size} rows)")
        Log.d("DB_DEBUG", "═══════════════════════════════════")
        favorites.forEach { favorite ->
            Log.d("DB_DEBUG", """
                CourseID: ${favorite.courseId}
                Timestamp: ${favorite.timestamp}
                ---
            """.trimIndent())
        }
    }

    suspend fun logFavoriteCoursesWithDetails() {
        val favoriteCourses = courseDao.getFavoriteCourses().first()
        Log.d("DB_DEBUG", "═══════════════════════════════════")
        Log.d("DB_DEBUG", "FAVORITE COURSES (${favoriteCourses.size} courses)")
        Log.d("DB_DEBUG", "═══════════════════════════════════")
        favoriteCourses.forEach { course ->
            Log.d("DB_DEBUG", """
                ID: ${course.id}
                Title: ${course.title}
                Level: ${course.level}
                Price: ${course.price}
                ⭐ FAVORITE: ${course.isFavorite}
                ---
            """.trimIndent())
        }
    }

    suspend fun logDatabaseStats() {
        val allCourses = courseDao.getAllCourses().first()
        val favorites = favoriteDao.getAllFavorites().first()
        val favoriteCourses = courseDao.getFavoriteCourses().first()

        Log.d("DB_DEBUG", """
            ═══════════════════════════════════
            📊 DATABASE STATISTICS
            ═══════════════════════════════════
            Total Courses: ${allCourses.size}
            Favorite Course IDs: ${favorites.size}
            Courses with isFavorite=true: ${favoriteCourses.size}

            Should match: ${favorites.size} = ${favoriteCourses.size}
            ═══════════════════════════════════
        """.trimIndent())
    }
}
