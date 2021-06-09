package com.example.mykotlinproject.model

import com.example.mykotlinproject.model.entities.Weather
import com.example.mykotlinproject.model.entities.getRussianCities
import com.example.mykotlinproject.model.entities.getWorldCities

class RepositoryImpl : Repository {
    override fun getWeatherFromServer(): Weather {
        return Weather()
    }
    override fun getWeatherFromLocalStorageRus(): List<Weather> {
        return getRussianCities()
    }
    override fun getWeatherFromLocalStorageWorld(): List<Weather> {
        return getWorldCities()
    }

}