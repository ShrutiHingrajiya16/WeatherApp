package com.example.weatherapp.storage

import android.content.SharedPreferences
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import javax.inject.Inject

val USER_PREFERENCE_NAME = "user_data"
val ALREADY_LOGGED_IN = "already_logged_in"

class AppPref constructor(
    var sharedPreferences: SharedPreferences,
) {

    fun setValue(key: String, value: Any) {
        val editor = sharedPreferences.edit()

        when (value) {
            is String -> editor.putString(key, value).apply()
            is Int -> editor.putInt(key, value).apply()
            is Boolean -> editor.putBoolean(key, value).apply()
        }
    }

    fun clearData() {
        sharedPreferences.edit().clear().apply()
    }
}