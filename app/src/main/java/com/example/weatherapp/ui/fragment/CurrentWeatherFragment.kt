package com.example.weatherapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weatherapp.BaseApplication
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentCurrentWeatherBinding
import com.example.weatherapp.storage.database.AppDB
import com.example.weatherapp.storage.tables.CurrentWeatherEntity
import com.example.weatherapp.ui.activity.HomeActivity
import com.example.weatherapp.ui.viewmodel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class CurrentWeatherFragment : Fragment() {
    private lateinit var binding: FragmentCurrentWeatherBinding
    private lateinit var viewModel: HomeViewModel
    private var currentWeatherData: CurrentWeatherEntity? = null

    @Inject
    lateinit var appDatabase: AppDB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity().application as BaseApplication).getApplicationComponent().inject(this)
        viewModel =
            ViewModelProvider(requireActivity() as HomeActivity).get(HomeViewModel::class.java)

        binding.pbCurrentWeather.visibility = View.VISIBLE
        binding.clContent.visibility = View.GONE

        viewModel.getCurrentWeatherResponse()
            .observe(requireActivity() as HomeActivity) { response ->
                response?.body()?.let {
                    viewModel.addCurrentWeatherDataToDB(it, appDatabase)
                }
            }

        viewModel.getCurrentWeatherDataFromDB(appDatabase)

        viewModel.currentWeatherResponseFromDatabase?.observe(
            requireActivity() as HomeActivity
        ) {
            currentWeatherData = it
            showCurrentWeatherData()
        }

    }

    private fun showCurrentWeatherData() {
        currentWeatherData?.let { data ->

            binding.pbCurrentWeather.visibility = View.GONE
            binding.clContent.visibility = View.VISIBLE
            binding.tvCityCountry.text = "${data.city}\n${data.country}"

            val iconText = data.icon

            iconText?.let {
                val icon = "http://openweathermap.org/img/w/$iconText.png"
                Glide.with(requireContext()).load(icon).into(binding.ivWeatherIcon)
            }

            binding.tvTemperature.text =
                getString(R.string.temp, data.temp?.toString())
            binding.tvTitle.text = data.main
            binding.tvDescription.text = data.description

            val sdf = SimpleDateFormat("hh:mm aa", Locale.getDefault())

            data.sunrise?.let {
                val netDate = Date(it.toLong() * 1000)
                binding.tvSunrise.text = sdf.format(netDate)
            }

            data.sunset?.let {
                val netDate = Date(it.toLong())
                binding.tvSunset.text = sdf.format(netDate)
            }

            val lastFetched = Date(data.dt ?: System.currentTimeMillis())

            binding.tvLastUpdated.text =
                getString(R.string.last_updated, sdf.format(lastFetched).toString())
        }
    }
}