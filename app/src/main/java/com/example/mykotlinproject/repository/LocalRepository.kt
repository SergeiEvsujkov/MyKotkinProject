package com.example.mykotlinproject.repository

import com.example.mykotlinproject.model.entities.Weather

interface LocalRepository {
    fun getAllHistory(): List<Weather>
    fun saveEntity(weather: Weather)
}