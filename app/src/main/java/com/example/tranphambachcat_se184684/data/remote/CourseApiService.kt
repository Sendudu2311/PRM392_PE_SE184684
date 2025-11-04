package com.example.tranphambachcat_se184684.data.remote

import com.example.tranphambachcat_se184684.data.model.Course
import com.example.tranphambachcat_se184684.data.model.CourseDetail
import com.example.tranphambachcat_se184684.data.model.PageResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API service for SkillVerse course endpoints
 * Base URL: https://skillverse.vn/api
 */
interface CourseApiService {

    /**
     * Get paginated list of courses
     * GET /api/courses
     */
    @GET("courses")
    suspend fun getCourses(
        @Query("status") status: String = "PUBLIC",
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 100,
        @Query("q") searchQuery: String? = null
    ): PageResponse<Course>

    /**
     * Get detailed course information by ID
     * GET /api/courses/{courseId}
     */
    @GET("courses/{courseId}")
    suspend fun getCourseById(
        @Path("courseId") courseId: Long
    ): CourseDetail

    companion object {
        const val BASE_URL = "https://skillverse.vn/api/"
    }
}
