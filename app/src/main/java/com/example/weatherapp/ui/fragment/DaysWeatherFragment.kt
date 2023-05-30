package com.example.weatherapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentDaysWeatherBinding
import com.example.weatherapp.retrofit.model.List
import com.example.weatherapp.retrofit.model.WeatherListData
import com.example.weatherapp.ui.activity.HomeActivity
import com.example.weatherapp.ui.adapter.DailyWeatherAdapter
import com.example.weatherapp.ui.adapter.DaysWeatherAdapter
import com.example.weatherapp.ui.viewmodel.HomeViewModel
import com.example.weatherapp.utils.isNetworkAvailable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

class DaysWeatherFragment : Fragment() {
    private lateinit var binding: FragmentDaysWeatherBinding
    private var selectedDayPosition = -1
    private lateinit var adapterDays: DaysWeatherAdapter
    private lateinit var adapterDaily: DailyWeatherAdapter
    private var daysWeatherListData: WeatherListData? = null
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDaysWeatherBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =
            ViewModelProvider(requireActivity() as HomeActivity).get(HomeViewModel::class.java)

        if (!requireActivity().isNetworkAvailable()) {
            binding.tvInternet.text = getString(R.string.check_your_internet_connectivity)
            binding.clNoInternet.visibility = View.VISIBLE
        }

        viewModel.getDaysWeatherResponse().observe(requireActivity() as HomeActivity) { response ->
            response?.let {
                daysWeatherListData = it.body()
                showDaysWeatherData()
                binding.clNoInternet.visibility = View.GONE
            } ?: run {
                binding.tvInternet.text = getString(R.string.no_data_available)
                binding.clNoInternet.visibility = View.VISIBLE
            }
        }

        viewModel.cityCountryData.observe(
            requireActivity() as HomeActivity
        ) {
            binding.tvCityCountry.text = "${it.first}\n${it.second}"
        }
    }

    private fun showDaysWeatherData() {
        showNextDays()
    }

    private fun showNextDays() {

        val titleArray = ArrayList<String>()
        val compareArray = ArrayList<String>()

        val calendar = GregorianCalendar();
        val sdf = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
        val sdf2 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        calendar.add(Calendar.DATE, 0)

        var day = sdf.format(calendar.time)
        var compareDay = sdf2.format(calendar.time)
        titleArray.add(day)
        compareArray.add(compareDay)

        for (i in 0..4) {
            calendar.add(Calendar.DATE, 1)
            day = sdf.format(calendar.time)
            compareDay = sdf2.format(calendar.time)
            titleArray.add(day)
            compareArray.add(compareDay)
        }

        setDaysAdapter(titleArray, compareArray)
        setDailyAdapter(compareArray)

    }

    private fun setDailyAdapter(compareArray: ArrayList<String>) {

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvDaily.layoutManager = linearLayoutManager

        adapterDaily = DailyWeatherAdapter { _: Int ->
        }

        binding.rvDaily.adapter = adapterDaily
        val arrayListData = java.util.ArrayList<List>()

        daysWeatherListData?.list?.map {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            it.dt?.let { a ->
                if (sdf.format(Date(a * 1000.toLong())).contains(compareArray[0])) {
                    arrayListData.add(it)
                }
            }
        }
        showDailyWeatherData(arrayListData)
    }

    private fun setDaysAdapter(titleArray: ArrayList<String>, compareArray: ArrayList<String>) {

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rvDays.layoutManager = linearLayoutManager

        adapterDays = DaysWeatherAdapter { position: Int, list: ArrayList<List> ->
            showSelectedDay(position, list)
        }
        adapterDays.setDaysList(titleArray, compareArray, daysWeatherListData)
        binding.rvDays.adapter = adapterDays

    }

    private fun showSelectedDay(position: Int, list: ArrayList<List>) {
        selectedDayPosition = position
        adapterDays.setSelection(selectedDayPosition)
        showDailyWeatherData(list)
    }

    private fun showDailyWeatherData(list: ArrayList<List>) {
        adapterDaily.setDailyWeatherList(list)
    }
}