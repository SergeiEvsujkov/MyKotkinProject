package com.example.mykotlinproject.utils

import com.example.mykotlinproject.model.entities.FactDTO
import com.example.mykotlinproject.model.entities.Weather
import com.example.mykotlinproject.model.entities.WeatherDTO
import com.example.mykotlinproject.model.entities.getDefaultCity

fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
    val fact: FactDTO = weatherDTO.fact!!
    return listOf(Weather(getDefaultCity(), fact.temp!!, fact.feels_like!!,
        fact.condition!!))
}