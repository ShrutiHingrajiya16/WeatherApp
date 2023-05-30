package com.example.weatherapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import com.example.weatherapp.R


fun Context.isNetworkAvailable(): Boolean {

    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        return connectivityManager.activeNetworkInfo?.isConnected ?: false
    }
}

fun Context.validateName(name: String): Boolean {

    return if (!ValidationUtils.validateName(name)) {
        Toast.makeText(this, getString(R.string.name_validate_message), Toast.LENGTH_SHORT).show()
        false
    } else true
}

fun Context.validateEmail(email: String): Boolean {

    if (!ValidationUtils.validateEmptyEmail(email)) {
        Toast.makeText(this, getString(R.string.email_empty_validate_message), Toast.LENGTH_SHORT)
            .show()
        return false
    }

    if (!ValidationUtils.validateEmailPattern(email)) {
        Toast.makeText(this, getString(R.string.email_validate_message), Toast.LENGTH_SHORT).show()
        return false
    }
    return true
}

fun Context.validatePassword(password: String): Boolean {
    if (!ValidationUtils.validateEmptyPassword(password)) {
        Toast.makeText(
            this,
            getString(R.string.password_empty_validate_message),
            Toast.LENGTH_SHORT
        )
            .show()
        return false
    }
    if (!ValidationUtils.validatePasswordLength(password)) {
        Toast.makeText(this, getString(R.string.password_validate_message), Toast.LENGTH_SHORT)
            .show()
        return false
    }
    return true
}