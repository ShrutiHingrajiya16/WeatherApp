package com.example.weatherapp.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherapp.ui.fragment.CurrentWeatherFragment
import com.example.weatherapp.ui.fragment.DaysWeatherFragment

class WeatherTabLayoutAdapter(
    context: Context,
    fragmentManager: FragmentManager,
    private val totalTabs: Int,
    lifecycle: Lifecycle,
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return totalTabs
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CurrentWeatherFragment()
            1 -> DaysWeatherFragment()
            else -> {
                CurrentWeatherFragment()
            }
        }
    }

}