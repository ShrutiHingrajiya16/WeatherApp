package com.example.weatherapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.retrofit.ApiInterface
import com.example.weatherapp.retrofit.model.CurrentWeatherData
import com.example.weatherapp.retrofit.model.WeatherListData
import com.example.weatherapp.storage.AppPref
import com.example.weatherapp.storage.database.AppDB
import com.example.weatherapp.storage.tables.CurrentWeatherEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val currentWeatherResponseLiveData = MutableLiveData<Response<CurrentWeatherData>>()
    var currentWeatherResponseFromDatabase: LiveData<CurrentWeatherEntity>? = null
    private val daysWeatherResponseLiveData = MutableLiveData<Response<WeatherListData>>()
    var cityCountryData = MutableLiveData<Pair<String, String>>()


    /*------------------------set city and country data --------------------*/

    fun setCityCountry(cityName: String, countryName: String) {
        cityCountryData.value = Pair(cityName, countryName)
    }

    /* --------------------------------for current weather ----------------------------*/

    fun getCurrentWeatherResponse(): MutableLiveData<Response<CurrentWeatherData>> {
        return currentWeatherResponseLiveData
    }

    fun requestForCurrentWeatherData(
        latitude: Double,
        longitude: Double,
        api_key: String,
        unit: String,
        apiInterface: ApiInterface,
    ) {
        apiInterface.getCurrentWeatherData(
            latitude, longitude, api_key, unit
        ).enqueue(object : Callback<CurrentWeatherData> {
            override fun onResponse(
                call: Call<CurrentWeatherData>,
                response: Response<CurrentWeatherData>,
            ) {
                if (response.isSuccessful) {
                    Log.e("Response_", response.body().toString())
                    currentWeatherResponseLiveData.value = response
                }
            }

            override fun onFailure(call: Call<CurrentWeatherData>, t: Throwable) {
                Log.e("Error_", t.localizedMessage?.toString() + "_")
                currentWeatherResponseLiveData.value = null
            }
        })
    }

    /* ------------------------ add current weather data to db ----------------------------*/

    fun addCurrentWeatherDataToDB(
        data: CurrentWeatherData,
        appDatabase: AppDB,
    ) {

        val weatherData = CurrentWeatherEntity(
            id = 1,
            city = cityCountryData.value?.first,
            country = cityCountryData.value?.second,
            temp = data.main?.temp,
            icon = data.weather[0].icon,
            sunset = data.sys?.sunset,
            sunrise = data.sys?.sunrise,
            main = data.weather[0].main,
            description = data.weather[0].description,
            dt = System.currentTimeMillis()
        )

        val a = appDatabase.currentWeatherDao().getItemById(id = 1)
        if (a == null) {
            appDatabase.currentWeatherDao().insertSingleData(weatherData)
        } else {
            appDatabase.currentWeatherDao().updateSingleData(
                1,
                cityCountryData.value?.first.toString(),
                cityCountryData.value?.second.toString(),
                data.main?.temp,
                data.weather[0].icon,
                data.sys?.sunset,
                data.sys?.sunrise,
                data.weather[0].main,
                data.weather[0].description,
                System.currentTimeMillis()
            )
        }
    }

    fun getCurrentWeatherDataFromDB(appDatabase: AppDB) {
        currentWeatherResponseFromDatabase = appDatabase.currentWeatherDao().getData()
    }

    /*---------------------------- for days weather data--------------------*/


    fun getDaysWeatherResponse(): MutableLiveData<Response<WeatherListData>> {
        return daysWeatherResponseLiveData
    }

    fun requestForDaysWeatherData(
        latitude: Double,
        longitude: Double,
        api_key: String,
        unit: String,
        apiInterface: ApiInterface,
    ) {
        apiInterface.getWeatherListData(
            latitude, longitude, api_key, unit
        ).enqueue(object : Callback<WeatherListData> {
            override fun onResponse(
                call: Call<WeatherListData>,
                response: Response<WeatherListData>,
            ) {
                if (response.isSuccessful) {
                    daysWeatherResponseLiveData.value = response
                }
            }

            override fun onFailure(call: Call<WeatherListData>, t: Throwable) {
                Log.e("Error_", t.localizedMessage?.toString() + "_")
                daysWeatherResponseLiveData.value = null
            }
        })
    }

    fun logout(appDatabase: AppDB, appPref: AppPref) {
        appDatabase.currentWeatherDao().deleteAllData()
        appPref.clearData()
    }

}