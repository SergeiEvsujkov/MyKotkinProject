package com.example.mykotlinproject.model

import com.example.mykotlinproject.model.entities.Weather
import com.example.mykotlinproject.model.entities.getRussianCities
import com.example.mykotlinproject.model.entities.getWorldCities
import com.example.mykotlinproject.model.interfaces.Repository

class RepositoryImpl : Repository {

    override fun getWeatherFromServer() = Weather()
    override fun getWeatherFromLocalStorageRus() = getRussianCities()
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()

}