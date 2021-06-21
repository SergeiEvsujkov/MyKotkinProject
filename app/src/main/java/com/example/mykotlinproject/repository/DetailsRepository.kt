package com.example.mykotlinproject.repository

import com.example.mykotlinproject.model.entities.WeatherDTO


interface DetailsRepository {
    fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    )
}

