package com.example.weatherapp.retrofit


import com.example.weatherapp.retrofit.model.CurrentWeatherData
import com.example.weatherapp.retrofit.model.WeatherListData
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    /**
     * for getting weather data
     */
    @GET("data/2.5/weather")
    fun getCurrentWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lng: Double,
        @Query("appid") appid: String,
        @Query("units") units: String,

        ): Call<CurrentWeatherData>

    /**
     * for getting weather data
     */
    @GET("data/2.5/forecast")
    fun getWeatherListData(
        @Query("lat") lat: Double,
        @Query("lon") lng: Double,
        @Query("appid") appid: String,
        @Query("units") units: String,

        ): Call<WeatherListData>
}