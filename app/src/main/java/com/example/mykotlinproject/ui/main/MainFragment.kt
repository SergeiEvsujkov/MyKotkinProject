package com.example.mykotlinproject.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mykotlinproject.R
import com.example.mykotlinproject.adapters.MainFragmentAdapter
import com.example.mykotlinproject.databinding.FragmentMainBinding
import com.example.mykotlinproject.model.entities.Weather
import com.example.mykotlinproject.ui.details.DetailsFragment
import com.example.mykotlinproject.viewmodel.AppState
import com.example.mykotlinproject.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private val adapter = MainFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .replace(R.id.container, DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                    }))
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
        viewModel.getLiveData().observe(viewLifecycleOwner, {
            renderData(it)
        })
        viewModel.getWeatherFromLocalSourceRus()
    }

    private fun changeWeatherDataSet() = when (isDataSetRus) {
        true -> {
            viewModel.getWeatherFromLocalSourceWorld()
            mainFragmentFAB.setImageResource(R.drawable.ic_russia)
            mainFragmentFAB.showSnackBarNoAction(R.string.world)
        }
        else -> {
            viewModel.getWeatherFromLocalSourceRus()
            mainFragmentFAB.setImageResource(R.drawable.ic_earth)
            mainFragmentFAB.showSnackBarNoAction(R.string.russia)
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
                mainFragmentRootView.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    {
                        if (!isDataSetRus)
                            viewModel.getWeatherFromLocalSourceRus() else
                            viewModel.getWeatherFromLocalSourceWorld()
                    })

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

    private fun View.showSnackBar(
        text: String,
        actionText: String,
        action: (View) -> Unit,
        length: Int = Snackbar.LENGTH_INDEFINITE
    ) {
        Snackbar.make(this, text, length).setAction(actionText, action).show()
    }

    private fun View.showSnackBarNoAction(
        textFromRes: Int,
        length: Int = Snackbar.LENGTH_SHORT
    ) {
        Snackbar.make(this, resources.getString(textFromRes), length).show()
    }
}
