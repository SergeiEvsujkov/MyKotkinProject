package com.example.mykotlinproject.ui.details

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mykotlinproject.R
import com.example.mykotlinproject.databinding.FragmentDetailsBinding
import com.example.mykotlinproject.model.entities.City
import com.example.mykotlinproject.model.entities.Weather
import com.example.mykotlinproject.utils.showSnackBar
import com.example.mykotlinproject.viewmodel.AppState
import com.example.mykotlinproject.viewmodel.DetailsViewModel
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_details.*


private const val IS_ICON_KEY = "LIST_OF_ICON_KEY"

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather
    private var isIconShow: Boolean = true
    //private var switch : Int = R.id.switch1


    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            renderData(it)
        })
        viewModel.detailsLiveData.observe(viewLifecycleOwner, Observer {
            renderData(it)
        })
        viewModel.getWeatherFromRemoteSource(
            weatherBundle.city.lat,
            weatherBundle.city.lon
        )

        showIcon()
        switch1.setOnCheckedChangeListener { _, isChecked ->

            val message = if (isChecked) "Иконка включена" else "Иконка выключена"
            isIconShow = isChecked
            saveIconShow(isChecked)
            Toast.makeText(
                context, message,

                Toast.LENGTH_SHORT
            ).show()

        }
    }

    private fun showIcon() {
        activity?.let {
            isIconShow = it.getPreferences(Context.MODE_PRIVATE).getBoolean(IS_ICON_KEY, true)
            switch1.isChecked = isIconShow
        }
    }

    private fun saveIconShow(isIconShow: Boolean) {
        activity?.let {
            with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                putBoolean(IS_ICON_KEY, isIconShow)
                apply()
            }
        }
    }


    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.mainView.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE
                setWeather(appState.weatherData[0])
            }
            is AppState.Loading -> {
                binding.mainView.visibility = View.GONE
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.mainView.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE
                binding.mainView.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    {
                        viewModel.detailsLiveData.observe(viewLifecycleOwner, Observer {
                            renderData(it)
                        })
                        viewModel.getWeatherFromRemoteSource(
                            weatherBundle.city.lat,
                            weatherBundle.city.lon
                        )
                    })
            }
        }
    }

    private fun setWeather(weather: Weather) {
        val city = weatherBundle.city
        saveCity(city, weather)
        binding.cityName.text = city.city
        binding.cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            city.lat.toString(),
            city.lon.toString()
        )
        binding.temperatureValue.text = weather.temperature.toString()
        binding.feelsLikeValue.text = weather.feelsLike.toString()
        binding.weatherCondition.text =
            getText(
                resources.getIdentifier(
                    weather.condition,
                    "string",
                    "com.example.mykotlinproject"
                )
            )
        Picasso
            .get()
            .load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
            .into(headerIcon)

        if (isIconShow) {
            weather.icon?.let {
                GlideToVectorYou.justLoadImage(
                    activity,
                    Uri.parse("https://yastatic.net/weather/i/icons/blueye/color/svg/${it}.svg"),
                    weatherIcon
                )
            }
        }

    }

    private fun saveCity(
        city: City,
        weather: Weather
    ) {
        viewModel.saveCityToDB(
            Weather(
                city,
                weather.temperature,
                weather.feelsLike,
                weather.condition
            )
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val BUNDLE_EXTRA = "weather"
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}

