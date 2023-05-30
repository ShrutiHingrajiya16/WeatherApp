package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.dagger.ApplicationComponent
import com.example.weatherapp.dagger.ApplicationModule
import com.example.weatherapp.dagger.DaggerApplicationComponent

class BaseApplication : Application() {

    companion object {
        lateinit var instance: BaseApplication
    }

    private val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
    }

    override fun onCreate() {
        super.onCreate()
        setUp()
    }

    private fun setUp() {
        instance = this
    }

    fun getApplicationComponent(): ApplicationComponent {
        return component
    }
}