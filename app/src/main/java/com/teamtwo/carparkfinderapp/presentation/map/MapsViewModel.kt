package com.teamtwo.carparkfinderapp.presentation.map

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtwo.carparkfinderapp.domain.model.Carpark
import com.teamtwo.carparkfinderapp.domain.repository.CarparkRepository
import com.teamtwo.carparkfinderapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// using hilt and dagger to inject the repository from the domain layer

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val repository: CarparkRepository
): ViewModel() {

    // create private instance of MapState
    private val _mapState = MutableStateFlow(MapState())
    // open mapState to allow read only accesses to state by other classes
    val mapState : StateFlow<MapState> = _mapState.asStateFlow()

    // all the carparks in a list
    val cparkList = mutableStateOf<List<Carpark>>(listOf())
    // all the bookmarked carparks in a list
    val cparkBookmarkList = mutableStateOf<List<Carpark>>(listOf())

    // states for the rendering of list
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    init {
        loadCarparkList()
        loadBookmarkedCarparkList()
    }

    // handle different events
    fun onEvent(event: MapEvent) {
        when(event) {
            is MapEvent.ToggleBookmarkView -> {
                // toggle state of bookmark view
                _mapState.update { currentState ->
                    currentState.copy(
                        isBookmarkView = !currentState.isBookmarkView
                    )
                }
            }
        }
    }

    // function to load all carparks into state
    fun loadCarparkList() {
        println("Making api request...")
        // launch coroutine to make api request
        viewModelScope.launch {
            // start loading
            isLoading.value = true

            // api call to get carparks where bookmark flag is set
            val result = repository.getCarparks()

            when(result) {
                is Resource.Success -> {
                    loadError.value = ""
                    isLoading.value = false
                    cparkList.value += result.data ?: emptyList()
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }

    // function to load all bookmarked carparks into state
    fun loadBookmarkedCarparkList() {
        println("Making api request...")
        // launch coroutine to make api request
        viewModelScope.launch {
            // start loading
            isLoading.value = true

            // api call to get carparks where bookmark flag is set
            val result = repository.getBookmarkedCarparks()

            when(result) {
                is Resource.Success -> {
                    loadError.value = ""
                    isLoading.value = false
                    cparkBookmarkList.value = result.data ?: emptyList()
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }
}