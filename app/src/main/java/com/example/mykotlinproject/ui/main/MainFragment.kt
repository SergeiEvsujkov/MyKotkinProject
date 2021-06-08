package com.example.mykotlinproject.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mykotlinproject.R
import com.example.mykotlinproject.databinding.FragmentDetailsBinding

import com.example.mykotlinproject.model.AppState

import com.example.mykotlinproject.model.entities.Weather
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        lifecycle.addObserver(viewModel)

        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })

        viewModel.getLifeCycleData().observe(viewLifecycleOwner, {
            Snackbar
                .make(binding.mainView, it, Snackbar.LENGTH_INDEFINITE)
                .show()
        })
        viewModel.getWeather()

        //binding.includedLayout.includedTextView.text = ""
    }

    private fun renderData(appState: AppState) = with(binding) {
        when (appState) {
            is AppState.Success -> {
                val weatherData = appState.weatherData
                loadingLayout.visibility = View.GONE
                setData(weatherData)
            }
            is AppState.Loading -> {
                loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                loadingLayout.visibility = View.GONE
                val error = appState.error
                Snackbar
                    .make(mainView, "Ошибка получения данных", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getWeather() }
                    .show()
            }
        }
    }

    private fun setData(weatherData: Weather) = with(binding) {
        cityName.text = weatherData.city.city
        cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            weatherData.city.lat.toString(),
            weatherData.city.lon.toString()
        )
        temperatureValue.text = weatherData.temperature.toString()
        feelsLikeValue.text = weatherData.feelsLike.toString()
    }
}