package com.teamtwo.carparkfinderapp.presentation.carparkdetail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtwo.carparkfinderapp.domain.model.Availability
import com.teamtwo.carparkfinderapp.domain.model.Carpark
import com.teamtwo.carparkfinderapp.domain.repository.AvailabilityRepository
import com.teamtwo.carparkfinderapp.domain.repository.CarparkRepository
import com.teamtwo.carparkfinderapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarparkDetailViewModel @Inject constructor(
    private val repository: CarparkRepository,
    private val repository2: AvailabilityRepository
) : ViewModel() {

    // dummy carpark to initialise the state
    val dummyCarpark = Carpark(
        -1,
        "LOL",
        "THIS CAR PARK DOES NOT EXIST",
        1.3010626054202958,
        103.85411771659147,
        "ELECTRONIC PARKING",
        "BASEMENT CAR PARK",
        1,
        1.0.toFloat(),
        "NEVER",
        "NO",
        "NOPE",
        0
    )

    val dummyAvailability = Availability(
        carparkNumber = "",
        updateDateTime = "No data for this carpark",
        lotType = "No data for this carpark",
        totalLots = 0,
        availableLots = 0
    )

    // states for holding retrieved carpark and availabilty information
    val cparkState = mutableStateOf<Carpark>(dummyCarpark)
    val availabilityState = mutableStateOf<Availability>(dummyAvailability)

    // states for the loading of list
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var toggleAvailability = mutableStateOf(false)


    // function to send an edit query to the DAO to change the bookmarked value
    fun setBookmark(flag: Int, id: Int) {
        viewModelScope.launch {
            repository.updateBookmark(flag, id)
        }
    }

    // function to load data into the list state item
    fun loadCarpark(id: Int) {
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
                    cparkState.value = result.data?.get(id) ?: dummyCarpark
                    println("detail list loaded")
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                    println("detail list not loaded")
                }
            }
        }
    }

    fun loadAvailability(cparkNo: String) {
        println("Making api request...")
        // launch coroutine to make api request
        viewModelScope.launch {
            // start loading
            isLoading.value = true

            // api call to get carpark list
            val result = repository2.getAvailability(cparkNo)

            when(result) {
                is Resource.Success -> {
                    loadError.value = ""
                    isLoading.value = false
                    // find matching value of carparkNumber in the list, return entry to the state
                    availabilityState.value = result.data?.find { availability ->
                        cparkNo.equals(availability.carparkNumber) } ?: dummyAvailability
                    println("availability loaded")
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                    println("availability not loaded")
                }
            }
        }
    }
}