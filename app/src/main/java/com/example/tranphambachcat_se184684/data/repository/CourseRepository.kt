package com.example.tranphambachcat_se184684.data.repository

import android.util.Log
import com.example.tranphambachcat_se184684.data.local.CourseDao
import com.example.tranphambachcat_se184684.data.local.FavoriteDao
import com.example.tranphambachcat_se184684.data.model.Course
import com.example.tranphambachcat_se184684.data.model.CourseDetail
import com.example.tranphambachcat_se184684.data.model.Favorite
import com.example.tranphambachcat_se184684.data.remote.CourseApiService
import com.example.tranphambachcat_se184684.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository implementing offline-first pattern
 * Fetches from network and caches to local database
 */
@Singleton
class CourseRepository @Inject constructor(
    private val apiService: CourseApiService,
    private val courseDao: CourseDao,
    private val favoriteDao: FavoriteDao
) {

    /**
     * Get all courses with offline-first approach
     * Returns cached data immediately while fetching fresh data
     */
    fun getCourses(forceRefresh: Boolean = false): Flow<Resource<List<Course>>> = flow {
        Log.d(TAG, "🔄 getCourses() called, forceRefresh=$forceRefresh")

        // Emit loading state
        emit(Resource.Loading())
        Log.d(TAG, "⏳ Emitted Loading state")

        // Fetch from network first
        try {
            Log.d(TAG, "🌐 Calling API: ${CourseApiService.BASE_URL}courses")
            val response = apiService.getCourses(status = "PUBLIC", size = 100)

            Log.d(TAG, "✅ API Response received:")
            Log.d(TAG, "   - Total items: ${response.total}")
            Log.d(TAG, "   - Page: ${response.page}")
            Log.d(TAG, "   - Size: ${response.size}")
            Log.d(TAG, "   - Items count: ${response.items.size}")

            if (response.items.isEmpty()) {
                Log.w(TAG, "⚠️ API returned empty list!")
                emit(Resource.Error("API returned no courses"))
                return@flow
            }

            // Log first course for debugging
            response.items.firstOrNull()?.let { firstCourse ->
                Log.d(TAG, "📘 Sample course:")
                Log.d(TAG, "   - ID: ${firstCourse.id}")
                Log.d(TAG, "   - Title: ${firstCourse.title}")
                Log.d(TAG, "   - Level: ${firstCourse.level}")
                Log.d(TAG, "   - Thumbnail: ${firstCourse.thumbnailUrl}")
            }

            // Update courses with favorite status from database
            Log.d(TAG, "🔄 Merging favorite status...")
            val coursesWithFavorites = response.items.map { course ->
                val isFavorite = favoriteDao.isFavorite(course.id)
                course.copy(isFavorite = isFavorite)
            }
            Log.d(TAG, "✅ Favorite status merged for ${coursesWithFavorites.size} courses")

            // Cache to database
            Log.d(TAG, "💾 Saving to database...")
            courseDao.deleteAllCourses()
            courseDao.insertCourses(coursesWithFavorites)
            Log.d(TAG, "✅ Saved ${coursesWithFavorites.size} courses to database")

            // Emit fresh data
            emit(Resource.Success(coursesWithFavorites))
            Log.d(TAG, "✅ Emitted Success with ${coursesWithFavorites.size} courses")

        } catch (e: Exception) {
            Log.e(TAG, "❌ Network error: ${e.javaClass.simpleName}")
            Log.e(TAG, "   Message: ${e.message}")
            e.printStackTrace()

            // Try to load from cache
            try {
                Log.d(TAG, "📦 Attempting to load from cache...")
                val cachedCourses = courseDao.getAllCourses()
                cachedCourses.collect { courses ->
                    if (courses.isNotEmpty()) {
                        Log.d(TAG, "📦 Loaded ${courses.size} courses from cache")
                        emit(Resource.Success(courses))
                    } else {
                        Log.w(TAG, "❌ Cache is also empty!")
                        emit(Resource.Error(
                            message = "Network error: ${e.message}\nCache is empty. Check internet connection."
                        ))
                    }
                }
            } catch (cacheError: Exception) {
                Log.e(TAG, "❌ Cache error: ${cacheError.message}")
                emit(Resource.Error(
                    message = "Failed to load courses: ${e.message}"
                ))
            }
        }
    }

    /**
     * Get course detail by ID
     */
    fun getCourseById(courseId: Long): Flow<Resource<CourseDetail>> = flow {
        Log.d(TAG, "🔍 getCourseById($courseId) called")
        emit(Resource.Loading())

        // Try to get from cache first
        val cachedCourse = courseDao.getCourseById(courseId)
        if (cachedCourse != null) {
            Log.d(TAG, "📦 Found course in cache: ${cachedCourse.title}")
            // Convert to CourseDetail (simplified)
            val detail = CourseDetail(
                id = cachedCourse.id,
                title = cachedCourse.title,
                description = cachedCourse.description,
                level = cachedCourse.level,
                status = cachedCourse.status,
                price = cachedCourse.price,
                currency = cachedCourse.currency,
                authorName = cachedCourse.authorName,
                thumbnailUrl = cachedCourse.thumbnailUrl,
                enrollmentCount = cachedCourse.enrollmentCount,
                createdAt = cachedCourse.createdAt,
                updatedAt = cachedCourse.updatedAt,
                publishedDate = cachedCourse.publishedDate,
                isFavorite = cachedCourse.isFavorite
            )
            emit(Resource.Success(detail))
        }

        // Fetch from network
        try {
            Log.d(TAG, "🌐 Fetching course detail from API...")
            val courseDetail = apiService.getCourseById(courseId)
            Log.d(TAG, "✅ Received course detail: ${courseDetail.title}")
            Log.d(TAG, "   - Modules: ${courseDetail.modules?.size ?: 0}")

            val isFavorite = favoriteDao.isFavorite(courseId)
            courseDetail.isFavorite = isFavorite

            // Update cache
            courseDao.insertCourse(courseDetail.toCourse(isFavorite))

            emit(Resource.Success(courseDetail))
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to fetch course detail: ${e.message}")
            if (cachedCourse == null) {
                emit(Resource.Error("Failed to load course: ${e.message}"))
            } else {
                emit(Resource.Error(
                    message = "Network error: ${e.message}. Showing cached data.",
                    data = null
                ))
            }
        }
    }

    /**
     * Search courses
     */
    fun searchCourses(query: String, level: String?): Flow<List<Course>> {
        Log.d(TAG, "🔍 searchCourses: query='$query', level='$level'")
        return courseDao.searchCourses(query, level)
    }

    /**
     * Get all available course levels
     */
    fun getAllLevels(): Flow<List<String>> {
        return courseDao.getAllLevels()
    }

    /**
     * Get favorite courses
     */
    fun getFavoriteCourses(): Flow<List<Course>> {
        Log.d(TAG, "⭐ getFavoriteCourses called")
        return courseDao.getFavoriteCourses()
    }

    /**
     * Search favorite courses
     */
    fun searchFavoriteCourses(query: String, level: String?): Flow<List<Course>> {
        Log.d(TAG, "⭐🔍 searchFavoriteCourses: query='$query', level='$level'")
        return courseDao.searchFavoriteCourses(query, level)
    }

    /**
     * Toggle favorite status
     */
    suspend fun toggleFavorite(courseId: Long): Boolean {
        val isFavorite = favoriteDao.isFavorite(courseId)

        Log.d(TAG, "⭐ toggleFavorite($courseId): current=$isFavorite")

        if (isFavorite) {
            // Remove from favorites
            favoriteDao.deleteFavoriteById(courseId)
            courseDao.updateFavoriteStatus(courseId, false)
            Log.d(TAG, "❤️ Removed from favorites")
            return false
        } else {
            // Add to favorites
            favoriteDao.insertFavorite(Favorite(courseId))
            courseDao.updateFavoriteStatus(courseId, true)
            Log.d(TAG, "💚 Added to favorites")
            return true
        }
    }

    /**
     * Check if course is favorite
     */
    fun isFavorite(courseId: Long): Flow<Boolean> {
        return favoriteDao.isFavoriteFlow(courseId)
    }

    companion object {
        private const val TAG = "CourseRepository"
    }
}
