package com.example.tranphambachcat_se184684.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tranphambachcat_se184684.data.model.Course
import com.example.tranphambachcat_se184684.data.repository.CourseRepository
import com.example.tranphambachcat_se184684.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CourseListUiState(
    val courses: List<Course> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedLevel: String? = null,
    val availableLevels: List<String> = emptyList()
)

@HiltViewModel
class CourseListViewModel @Inject constructor(
    private val repository: CourseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CourseListUiState())
    val uiState: StateFlow<CourseListUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private val _selectedLevel = MutableStateFlow<String?>(null)

    init {
        loadCourses()
        loadLevels()
        observeSearchAndFilter()
    }

    private fun loadCourses() {
        viewModelScope.launch {
            repository.getCourses().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                courses = resource.data ?: emptyList(),
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                error = resource.message,
                                isLoading = false
                            )
                        }
                    }
                }
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
                    // Show all courses
                    loadCourses()
                } else {
                    // Apply search and filter
                    repository.searchCourses(query, level).collect { courses ->
                        _uiState.update {
                            it.copy(
                                courses = courses,
                                searchQuery = query,
                                selectedLevel = level,
                                isLoading = false
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

    fun refreshCourses() {
        viewModelScope.launch {
            repository.getCourses(forceRefresh = true).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                courses = resource.data ?: emptyList(),
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                error = resource.message,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
}
