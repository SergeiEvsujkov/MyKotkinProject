package com.example.mykotlinproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mykotlinproject.model.app.App.Companion.getHistoryDao
import com.example.mykotlinproject.repository.LocalRepository
import com.example.mykotlinproject.repository.LocalRepositoryImpl

class HistoryViewModel(
    val historyLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val historyRepository: LocalRepository =
        LocalRepositoryImpl(getHistoryDao())
) : ViewModel() {
    fun getAllHistory() {
        historyLiveData.value = AppState.Loading
        historyLiveData.value =
            AppState.Success(historyRepository.getAllHistory())
    }
}
