package com.example.tranphambachcat_se184684.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tranphambachcat_se184684.data.model.CourseDetail
import com.example.tranphambachcat_se184684.data.repository.CourseRepository
import com.example.tranphambachcat_se184684.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CourseDetailUiState(
    val course: CourseDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isFavorite: Boolean = false
)

@HiltViewModel
class CourseDetailViewModel @Inject constructor(
    private val repository: CourseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val courseId: Long = checkNotNull(savedStateHandle["courseId"])

    private val _uiState = MutableStateFlow(CourseDetailUiState())
    val uiState: StateFlow<CourseDetailUiState> = _uiState.asStateFlow()

    init {
        loadCourseDetail()
        observeFavoriteStatus()
    }

    private fun loadCourseDetail() {
        viewModelScope.launch {
            repository.getCourseById(courseId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                course = resource.data,
                                isLoading = false,
                                error = null,
                                isFavorite = resource.data?.isFavorite ?: false
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

    private fun observeFavoriteStatus() {
        viewModelScope.launch {
            repository.isFavorite(courseId).collect { isFavorite ->
                _uiState.update { it.copy(isFavorite = isFavorite) }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val newFavoriteStatus = repository.toggleFavorite(courseId)
            _uiState.update { it.copy(isFavorite = newFavoriteStatus) }
        }
    }
}
