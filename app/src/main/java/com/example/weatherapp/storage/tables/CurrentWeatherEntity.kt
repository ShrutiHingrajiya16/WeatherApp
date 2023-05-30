package com.example.weatherapp.storage.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TblCurrentWeather")
class CurrentWeatherEntity(
    @PrimaryKey(autoGenerate = true) var id1: Int? = null,
    @ColumnInfo(name = "id") val id: Int?,
    @ColumnInfo(name = "city") val city: String?,
    @ColumnInfo(name = "country") val country: String?,
    @ColumnInfo(name = "temp1") val temp: Double?,
    @ColumnInfo(name = "icon") val icon: String?,
    @ColumnInfo(name = "sunset") val sunset: Int?,
    @ColumnInfo(name = "sunrise") val sunrise: Int?,
    @ColumnInfo(name = "title") val main: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "lastFetched") val dt: Long?,
)