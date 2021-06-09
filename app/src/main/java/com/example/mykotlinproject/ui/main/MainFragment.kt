package com.example.mykotlinproject.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mykotlinproject.R
import com.example.mykotlinproject.adapters.MainFragmentAdapter
import com.example.mykotlinproject.databinding.FragmentMainBinding
import com.example.mykotlinproject.viewmodel.AppState
import com.example.mykotlinproject.model.entities.Weather
import com.example.mykotlinproject.ui.details.DetailsFragment
import com.example.mykotlinproject.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private val adapter = MainFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            val manager = activity?.supportFragmentManager
            if (manager != null) {
                val bundle = Bundle()
                bundle.putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                manager.beginTransaction()
                    .replace(R.id.container, DetailsFragment.newInstance(bundle))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }
    })
    private var isDataSetRus: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            renderData(it) })
        viewModel.getWeatherFromLocalSourceRus()
    }
    private fun changeWeatherDataSet() {
        if (isDataSetRus) {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        } else {
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        }

    }


    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                isDataSetRus = !isDataSetRus
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherData)
            }
            is AppState.Loading -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
               // isDataSetRus = !isDataSetRus
                //binding.mainFragmentLoadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.mainFragmentFAB, getString(R.string.error),
                        Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.reload)) {
                        //viewModel.getWeatherFromLocalSourceRus()

                        if (!isDataSetRus)
                            viewModel.getWeatherFromLocalSourceRus() else
                            viewModel.getWeatherFromLocalSourceWorld()
                        }
                    .show()
            }
        }
    }
    interface OnItemViewClickListener {
        fun onItemViewClick(weather: Weather)
    }
    companion object {
        fun newInstance() =
            MainFragment()
    }
}
