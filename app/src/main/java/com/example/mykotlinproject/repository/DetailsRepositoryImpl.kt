package com.example.mykotlinproject.repository

import com.example.mykotlinproject.model.entities.RemoteDataSource
import com.example.mykotlinproject.model.entities.WeatherDTO


class DetailsRepositoryImpl(private val remoteDataSource: RemoteDataSource) :
    DetailsRepository {
    override fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    ) {
        remoteDataSource.getWeatherDetails(lat, lon, callback)
    }
}


