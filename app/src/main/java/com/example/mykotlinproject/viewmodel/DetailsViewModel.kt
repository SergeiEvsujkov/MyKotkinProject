package com.example.mykotlinproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mykotlinproject.model.app.App.Companion.getHistoryDao
import com.example.mykotlinproject.model.entities.RemoteDataSource
import com.example.mykotlinproject.model.entities.Weather
import com.example.mykotlinproject.model.entities.WeatherDTO
import com.example.mykotlinproject.repository.DetailsRepository
import com.example.mykotlinproject.repository.DetailsRepositoryImpl
import com.example.mykotlinproject.repository.LocalRepository
import com.example.mykotlinproject.repository.LocalRepositoryImpl
import com.example.mykotlinproject.utils.convertDtoToModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class DetailsViewModel(
    val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryImpl: DetailsRepository =
        DetailsRepositoryImpl(RemoteDataSource()),
                private val historyRepository: LocalRepository =
            LocalRepositoryImpl(getHistoryDao())
) : ViewModel() {
    fun getLiveData() = detailsLiveData
    fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
        detailsLiveData.value = AppState.Loading
        detailsRepositoryImpl.getWeatherDetailsFromServer(lat, lon, callBack)
    }

    fun saveCityToDB(weather: Weather) {
        historyRepository.saveEntity(weather)
    }


    private val callBack = object :
        Callback<WeatherDTO> {
        override fun onResponse(call: Call<WeatherDTO>, response:
        Response<WeatherDTO>
        ) {
            val serverResponse: WeatherDTO? = response.body()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }
        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            detailsLiveData.postValue(AppState.Error(Throwable(t.message ?:
            REQUEST_ERROR)))
        }
        private fun checkResponse(serverResponse: WeatherDTO): AppState {
            val fact = serverResponse.fact
            return if (fact?.temp == null || fact.feels_like ==
                null || fact.condition.isNullOrEmpty() || fact.icon == null) {
                AppState.Error(Throwable(CORRUPTED_DATA))
            } else {
                AppState.Success(convertDtoToModel(serverResponse))
            }
        }
    }



}