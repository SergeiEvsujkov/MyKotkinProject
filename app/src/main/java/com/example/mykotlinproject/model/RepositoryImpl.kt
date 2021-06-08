package com.example.mykotlinproject.model

import com.example.mykotlinproject.model.entities.Weather

class RepositoryImpl : Repository {
    override fun getWeatherFromServer() = Weather()
    override fun getWeatherFromLocalStorage() = Weather()
}