package com.example.weatherapp.dagger

import com.example.weatherapp.ui.activity.HomeActivity
import com.example.weatherapp.ui.activity.SignInActivity
import com.example.weatherapp.ui.activity.SignUpActivity
import com.example.weatherapp.ui.activity.SplashActivity
import com.example.weatherapp.ui.fragment.CurrentWeatherFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun inject(activity: SplashActivity)
    fun inject(activity: SignUpActivity)
    fun inject(activity: SignInActivity)
    fun inject(activity: HomeActivity)
    fun inject(fragment: CurrentWeatherFragment)
}