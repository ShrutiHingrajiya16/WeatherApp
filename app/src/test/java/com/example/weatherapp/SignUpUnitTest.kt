package com.example.weatherapp

import com.example.weatherapp.utils.ValidationUtils
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SignUpUnitTest {

    /**
     * check name validation for empty name
     */
    @Test
    fun validateEmptyName(){
        val result = ValidationUtils.validateName("")
        assertEquals(false, result)
    }

    /**
     * check name validation for not empty name
     */
    @Test
    fun validateNotEmptyName(){
        val result = ValidationUtils.validateName("abc")
        assertEquals(true, result)
    }

    /**
     * check email validation for empty email
     */
    @Test
    fun validateEmptyEmail(){
        val result = ValidationUtils.validateEmptyEmail("")
        assertEquals(false, result)
    }

    /**
     * check email validation for not empty email
     */
    @Test
    fun validateNotEmptyEmail(){
        val result = ValidationUtils.validateEmptyEmail("xyz")
        assertEquals(true, result)
    }

    /**
     * check email pattern validation
     */
    @Test
    fun validateEmailPattern(){
        val result = ValidationUtils.validateEmailPattern("xyz@gmail.com")
        assertEquals(true, result)
    }

    /**
     * check password validation for empty password
     */
    @Test
    fun validateEmptyPassword(){
        val result = ValidationUtils.validateEmptyPassword("")
        assertEquals(false, result)
    }

    /**
     * check password validation for empty password
     */
    @Test
    fun validateNotEmptyPassword(){
        val result = ValidationUtils.validateEmptyPassword("1234")
        assertEquals(true, result)
    }

    /**
     * check password validation for password length
     */
    @Test
    fun validatePasswordLength(){
        val result = ValidationUtils.validatePasswordLength("12345678")
        assertEquals(true, result)
    }
}