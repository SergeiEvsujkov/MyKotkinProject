package com.example.mykotlinproject.ui.main

import androidx.lifecycle.*
import com.example.mykotlinproject.model.AppState
import com.example.mykotlinproject.model.Repository
import com.example.mykotlinproject.model.RepositoryImpl
import com.example.mykotlinproject.model.entities.Weather
import java.lang.Exception
import java.lang.Thread.sleep
import kotlin.random.Random

class MainViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData())
    : ViewModel(), LifecycleObserver {
    private val repository: Repository = RepositoryImpl()
    private val lifeCycleLiveData = MutableLiveData<String>()

    fun getLiveData() = liveDataToObserve

    fun getWeather() = getDataFromLocalSource()

    fun getData(): LiveData<AppState> {
        getDataFromLocalSource()
        return liveDataToObserve
    }

    fun getLifeCycleData() = lifeCycleLiveData

    fun getWeatherFromRemoteSource() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            val rand: Int = Random.nextInt(0,10)
            if (rand % 2 == 0) {
                liveDataToObserve.postValue(AppState.Success(repository.getWeatherFromLocalStorage()))
            } else {
                liveDataToObserve.postValue(AppState.Error(Throwable("Ошибка получения данных")))
            }
        }.start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onViewStart() {
        lifeCycleLiveData.value = "Start"
    }
}