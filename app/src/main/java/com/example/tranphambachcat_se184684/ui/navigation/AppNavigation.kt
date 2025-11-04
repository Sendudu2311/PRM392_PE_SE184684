package com.example.tranphambachcat_se184684.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tranphambachcat_se184684.ui.screens.detail.CourseDetailScreen
import com.example.tranphambachcat_se184684.ui.screens.favorites.FavoritesScreen
import com.example.tranphambachcat_se184684.ui.screens.list.CourseListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != null && !currentRoute.startsWith("course_detail")) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Courses") },
                        label = { Text("Courses") },
                        selected = currentRoute == Screen.CourseList.route,
                        onClick = {
                            navController.navigate(Screen.CourseList.route) {
                                popUpTo(Screen.CourseList.route) { inclusive = true }
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
                        label = { Text("Favorites") },
                        selected = currentRoute == Screen.Favorites.route,
                        onClick = {
                            navController.navigate(Screen.Favorites.route) {
                                popUpTo(Screen.CourseList.route)
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.CourseList.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.CourseList.route) {
                CourseListScreen(
                    onCourseClick = { courseId ->
                        navController.navigate(Screen.CourseDetail.createRoute(courseId))
                    }
                )
            }

            composable(
                route = Screen.CourseDetail.route,
                arguments = listOf(
                    navArgument("courseId") { type = NavType.LongType }
                )
            ) {
                CourseDetailScreen(
                    onBackClick = { navController.navigateUp() }
                )
            }

            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    onCourseClick = { courseId ->
                        navController.navigate(Screen.CourseDetail.createRoute(courseId))
                    }
                )
            }
        }
    }
}
