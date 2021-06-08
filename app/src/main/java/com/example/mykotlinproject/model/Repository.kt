package com.example.mykotlinproject.model

import com.example.mykotlinproject.model.entities.Weather

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorage(): Weather
}