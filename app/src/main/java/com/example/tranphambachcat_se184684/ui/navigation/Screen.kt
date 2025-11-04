package com.example.tranphambachcat_se184684.ui.navigation

sealed class Screen(val route: String) {
    object CourseList : Screen("course_list")
    object CourseDetail : Screen("course_detail/{courseId}") {
        fun createRoute(courseId: Long) = "course_detail/$courseId"
    }
    object Favorites : Screen("favorites")
}
