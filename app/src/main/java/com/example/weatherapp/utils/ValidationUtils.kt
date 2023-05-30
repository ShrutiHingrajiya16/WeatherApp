package com.example.weatherapp.utils

import androidx.core.util.PatternsCompat

object ValidationUtils {

    fun validateName(name: String): Boolean {
        if (name.isEmpty()) {
            return false
        }
        return true
    }

    fun validateEmptyEmail(email: String): Boolean {
        if (email.isEmpty()) {
            return false
        }
        return true
    }

    fun validateEmailPattern(email: String): Boolean {
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            return false
        }
        return true
    }

    fun validateEmptyPassword(password: String): Boolean {
        if (password.isEmpty()) {
            return false
        }
        return true
    }

    fun validatePasswordLength(password: String): Boolean {
        if (password.length < 8) {
            return false
        }
        return true
    }
}