package com.teamtwo.carparkfinderapp.presentation.carparklist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtwo.carparkfinderapp.domain.model.Carpark
import com.teamtwo.carparkfinderapp.domain.repository.CarparkRepository
import com.teamtwo.carparkfinderapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarparkListViewModel @Inject constructor(
    private val repository: CarparkRepository
) : ViewModel() {

    // all the carparks in a list
    val cparkList = mutableStateOf<List<Carpark>>(listOf())

    // cache list used for search results
    var cachedCparkList = listOf<Carpark>()

    // necessary for searching the list
    private var isSearchStarting = true            // true as long as search field is empty
    var isSearching= mutableStateOf(false)   // true as long as searches are displayed

    // states for the rendering of list
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    init {
        loadCarparkList()
    }

    // search the list of Carpark objects based on address
    fun searchCarparkList(query: String) {
        val listToSearch = if(isSearchStarting) {
            // text field is empty - display regular list
            cparkList.value
        } else {
            // if at least 1 char is typed into searchbar
            cachedCparkList
        }

        // launch coroutine so search does not execute on main thread
        viewModelScope.launch(Dispatchers.Default) {

            if(query.isEmpty()) {
                // reset the list to display in lazyColumn - display everything again
                cparkList.value = cachedCparkList
                isSearchStarting = true
                isSearching.value = false
                return@launch
            }

            // check if query has matching name
            val results = listToSearch.filter {
                it.address.contains(query.trim(), ignoreCase = true)
            }

            // cache the complete list, now currently searching
            if (isSearchStarting) {
                cachedCparkList = cparkList.value
                isSearchStarting = false
            }

            // displayed list only contains filtered entries now
            cparkList.value = results
            isSearching.value = true
        }
    }

    // function to load data into the list state item
    fun loadCarparkList() {
        println("Making api request...")
        // launch coroutine to make api request
        viewModelScope.launch {
            // start loading
            isLoading.value = true

            // api call to get carpark list
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
}
