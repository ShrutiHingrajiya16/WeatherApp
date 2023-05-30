package com.example.weatherapp.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.BaseApplication
import com.example.weatherapp.databinding.ActivitySplashBinding
import com.example.weatherapp.storage.ALREADY_LOGGED_IN
import com.example.weatherapp.storage.AppPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {
    private var flagUser = false

    private lateinit var binding: ActivitySplashBinding

    @Inject
    lateinit var appPref: AppPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    /**
     *
     */
    private fun initView() {
        (application as BaseApplication).getApplicationComponent().inject(this)

        GlobalScope.launch(Dispatchers.Main) {
            delay(3000)
            navigateUserToOtherScreen()
        }
    }

    /**
     * check if user is already signed in then go to home screen
     * else go to sign in screen
     */
    private fun navigateUserToOtherScreen() {
        flagUser = appPref.sharedPreferences.getBoolean(ALREADY_LOGGED_IN, false)

        if (flagUser) {
            navigateUserToHomeActivity()
        } else {
            navigateUserToSignInActivity()
        }
    }

    /**
     * method for navigate user to sign in screen
     */
    private fun navigateUserToSignInActivity() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * method for navigate user to home screen
     */
    private fun navigateUserToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}