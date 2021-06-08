package com.example.mykotlinproject.model

import com.example.mykotlinproject.model.entities.Weather

sealed class AppState {
    data class Success(val weatherData: Weather) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
