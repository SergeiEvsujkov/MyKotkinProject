package com.example.mykotlinproject.repository

import com.example.mykotlinproject.model.entities.Weather
import com.example.mykotlinproject.model.entities.getRussianCities
import com.example.mykotlinproject.model.entities.getWorldCities

class RepositoryImpl : Repository {

    override fun getWeatherFromServer() = Weather()
    override fun getWeatherFromLocalStorageRus() = getRussianCities()
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()

}