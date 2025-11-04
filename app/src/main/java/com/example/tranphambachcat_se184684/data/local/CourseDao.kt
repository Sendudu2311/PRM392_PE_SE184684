package com.example.tranphambachcat_se184684.data.local

import androidx.room.*
import com.example.tranphambachcat_se184684.data.model.Course
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    @Query("SELECT * FROM courses ORDER BY cacheTimestamp DESC")
    fun getAllCourses(): Flow<List<Course>>

    @Query("SELECT * FROM courses WHERE id = :courseId")
    suspend fun getCourseById(courseId: Long): Course?

    @Query("SELECT * FROM courses WHERE id = :courseId")
    fun getCourseByIdFlow(courseId: Long): Flow<Course?>

    @Query("SELECT * FROM courses WHERE isFavorite = 1 ORDER BY cacheTimestamp DESC")
    fun getFavoriteCourses(): Flow<List<Course>>

    @Query("""
        SELECT * FROM courses
        WHERE (title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%')
        AND (:level IS NULL OR level = :level)
        ORDER BY cacheTimestamp DESC
    """)
    fun searchCourses(query: String, level: String?): Flow<List<Course>>

    @Query("""
        SELECT * FROM courses
        WHERE isFavorite = 1
        AND (title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%')
        AND (:level IS NULL OR level = :level)
        ORDER BY cacheTimestamp DESC
    """)
    fun searchFavoriteCourses(query: String, level: String?): Flow<List<Course>>

    @Query("SELECT DISTINCT level FROM courses WHERE level IS NOT NULL ORDER BY level")
    fun getAllLevels(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Course)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<Course>)

    @Update
    suspend fun updateCourse(course: Course)

    @Query("UPDATE courses SET isFavorite = :isFavorite WHERE id = :courseId")
    suspend fun updateFavoriteStatus(courseId: Long, isFavorite: Boolean)

    @Delete
    suspend fun deleteCourse(course: Course)

    @Query("DELETE FROM courses")
    suspend fun deleteAllCourses()

    @Query("DELETE FROM courses WHERE cacheTimestamp < :timestamp")
    suspend fun deleteOldCourses(timestamp: Long)
}
