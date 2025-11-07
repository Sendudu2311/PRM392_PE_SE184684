package com.example.tranphambachcat_se184684.ui.screens.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tranphambachcat_se184684.ui.components.CourseCard
import com.example.tranphambachcat_se184684.ui.components.SearchAndFilterBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onCourseClick: (Long) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("My Favorites", color = androidx.compose.ui.graphics.Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color(0x1AFFFFFF),
                    titleContentColor = androidx.compose.ui.graphics.Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search and Filter
            SearchAndFilterBar(
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = viewModel::onSearchQueryChange,
                selectedLevel = uiState.selectedLevel,
                onLevelChange = viewModel::onLevelFilterChange,
                availableLevels = uiState.availableLevels
            )

            // Content
            if (uiState.favoriteCourses.isEmpty()) {
                // Empty State
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (uiState.searchQuery.isNotBlank() || uiState.selectedLevel != null) {
                                "No favorite courses match your filters"
                            } else {
                                "You have no favorites yet"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Start adding courses to your favorites!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                // Favorite Courses List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = uiState.favoriteCourses,
                        key = { it.id }
                    ) { course ->
                        CourseCard(
                            course = course,
                            onClick = { onCourseClick(course.id) }
                        )
                    }
                }
            }
        }
    }
}
