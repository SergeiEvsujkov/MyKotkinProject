package com.example.mykotlinproject.model.interfaces

import com.example.mykotlinproject.model.entities.Weather

interface OnItemViewClickListener {
    fun onItemViewClick(weather: Weather)
}