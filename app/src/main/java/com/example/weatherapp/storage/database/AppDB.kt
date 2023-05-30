package com.example.weatherapp.storage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.retrofit.model.CurrentWeatherData
import com.example.weatherapp.storage.dao.CurrentWeatherDao
import com.example.weatherapp.storage.dao.UserDao
import com.example.weatherapp.storage.tables.CurrentWeatherEntity
import com.example.weatherapp.storage.tables.UserEntity


@Database(
    entities = [UserEntity::class, CurrentWeatherEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDB : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun currentWeatherDao(): CurrentWeatherDao
}