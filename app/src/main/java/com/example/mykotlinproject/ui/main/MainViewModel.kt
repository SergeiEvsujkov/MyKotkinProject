package com.example.mykotlinproject.ui.main

import androidx.lifecycle.*
import com.example.mykotlinproject.model.AppState
import com.example.mykotlinproject.model.Repository
import com.example.mykotlinproject.model.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData())
    : ViewModel(), LifecycleObserver {
    private val repositoryImpl: Repository = RepositoryImpl()
    private val lifeCycleLiveData = MutableLiveData<String>()

    fun getLiveData() = liveDataToObserve
    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(isRussian = true)
    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)
    fun getWeatherFromRemoteSource() = getDataFromLocalSource(isRussian = true)
    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserve.value = AppState.Loading

        Thread {
            sleep(1000)
            val rand: Int = (0..10).random()
            if (rand  != 1) {
                liveDataToObserve.postValue(AppState.Success(if (isRussian)
                    repositoryImpl.getWeatherFromLocalStorageRus() else
                    repositoryImpl.getWeatherFromLocalStorageWorld()))
            } else {
                liveDataToObserve.postValue(AppState.Error(Throwable()))

            }
        }.start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onViewStart() {
        lifeCycleLiveData.value = "Start"
    }
}