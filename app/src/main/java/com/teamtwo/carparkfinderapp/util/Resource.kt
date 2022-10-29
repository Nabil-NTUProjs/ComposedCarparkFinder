package com.teamtwo.carparkfinderapp.util

// wrapper class for api call responses - sent to the viewModel
// data and message null by default

typealias SimpleResource = Resource<Unit>

sealed class Resource<T>(val data: T? , val message: String? = null) {

    // if successful, data is expected (mandatory)
    class Success<T>(data: T) : Resource<T>(data)

    // if failure, data is null usually, but we pass a message
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    // to display a progress bar while data is being fetched
    class Loading<T>(data: T? = null): Resource<T>(data)
}
