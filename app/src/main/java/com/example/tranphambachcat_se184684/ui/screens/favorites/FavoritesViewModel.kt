package com.example.tranphambachcat_se184684.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tranphambachcat_se184684.data.model.Course
import com.example.tranphambachcat_se184684.data.repository.CourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoritesUiState(
    val favoriteCourses: List<Course> = emptyList(),
    val searchQuery: String = "",
    val selectedLevel: String? = null,
    val availableLevels: List<String> = emptyList()
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: CourseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private val _selectedLevel = MutableStateFlow<String?>(null)

    init {
        loadFavorites()
        loadLevels()
        observeSearchAndFilter()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            repository.getFavoriteCourses().collect { courses ->
                _uiState.update { it.copy(favoriteCourses = courses) }
            }
        }
    }

    private fun loadLevels() {
        viewModelScope.launch {
            repository.getAllLevels().collect { levels ->
                _uiState.update { it.copy(availableLevels = levels) }
            }
        }
    }

    private fun observeSearchAndFilter() {
        viewModelScope.launch {
            combine(_searchQuery, _selectedLevel) { query, level ->
                Pair(query, level)
            }.collectLatest { (query, level) ->
                if (query.isBlank() && level == null) {
                    // Show all favorites
                    loadFavorites()
                } else {
                    // Apply search and filter
                    repository.searchFavoriteCourses(query, level).collect { courses ->
                        _uiState.update {
                            it.copy(
                                favoriteCourses = courses,
                                searchQuery = query,
                                selectedLevel = level
                            )
                        }
                    }
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onLevelFilterChange(level: String?) {
        _selectedLevel.value = level
        _uiState.update { it.copy(selectedLevel = level) }
    }
}
