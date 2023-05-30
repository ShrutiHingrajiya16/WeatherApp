package com.example.weatherapp.storage.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapp.storage.tables.CurrentWeatherEntity

@Dao
interface CurrentWeatherDao {

    @Query("SELECT * FROM TblCurrentWeather")
    fun getData(): LiveData<CurrentWeatherEntity>

    @Query("SELECT * from TblCurrentWeather WHERE id= :id")
    fun getItemById(id: Int): CurrentWeatherEntity?

    @Query("SELECT * from TblCurrentWeather")
    fun getItemList(): List<CurrentWeatherEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSingleData(item: CurrentWeatherEntity)

    //@Update(onConflict = OnConflictStrategy.REPLACE)
    @Query("UPDATE TblCurrentWeather SET city=:city , country =:country, temp1=:temp , icon=:icon , sunset=:sunset ,sunrise=:sunrise , title=:title,description=:description , lastFetched=:lastFetched WHERE id = :id")
    //fun updateSingleData(item: CurrentWeatherEntity)
    fun updateSingleData(
        id: Int, city: String, country: String, temp: Double?,
        icon: String?,
        sunset: Int?,
        sunrise: Int?,
        title: String?,
        description: String?,
        lastFetched: Long,
    )

    @Query("DELETE FROM TblCurrentWeather")
    fun deleteAllData()

}