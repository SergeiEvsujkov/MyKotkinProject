package com.example.mykotlinproject.model.entities

import kotlin.random.Random

data class Weather(
    val city: City = City("Москва", 55.755826, 37.61729),
    var temperature: Int = Random.nextInt(-20,30),
    var feelsLike: Int = temperature + Random.nextInt(-3, 3)
)

//fun getDefaultCity() = City("Москва", 55.755826, 37.617299900000035)




