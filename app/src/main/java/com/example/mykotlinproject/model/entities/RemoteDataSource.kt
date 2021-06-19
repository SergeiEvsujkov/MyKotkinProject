package com.example.mykotlinproject.model.entities

import com.example.mykotlinproject.BuildConfig
import com.example.mykotlinproject.model.interfaces.WeatherAPI
import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val REQUEST_API_KEY = "X-Yandex-API-Key"
class RemoteDataSource {
    private val weatherApi = Retrofit.Builder()
        .baseUrl("https://api.weather.yandex.ru/")
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(WeatherAPI::class.java)
    fun getWeatherDetails(lat: Double, lon: Double, callback:
    Callback<WeatherDTO>
    ) {
        weatherApi.getWeather(BuildConfig.WEATHER_API_KEY, lat,
            lon).enqueue(callback)
    }
}

