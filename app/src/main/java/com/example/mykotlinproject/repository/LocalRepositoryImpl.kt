package com.example.mykotlinproject.repository

import com.example.mykotlinproject.model.entities.Weather
import com.example.mykotlinproject.model.room.HistoryDao
import com.example.mykotlinproject.utils.convertHistoryEntityToWeather
import com.example.mykotlinproject.utils.convertWeatherToEntity

class LocalRepositoryImpl(private val localDataSource: HistoryDao) :
    LocalRepository {
    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }
    override fun saveEntity(weather: Weather) {
        localDataSource.insert(convertWeatherToEntity(weather))
    }
}