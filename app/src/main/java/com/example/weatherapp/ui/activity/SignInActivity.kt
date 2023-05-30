package com.example.weatherapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.BaseApplication
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivitySignInBinding
import com.example.weatherapp.storage.ALREADY_LOGGED_IN
import com.example.weatherapp.storage.AppPref
import com.example.weatherapp.storage.database.AppDB
import com.example.weatherapp.utils.validateEmail
import com.example.weatherapp.utils.validatePassword
import javax.inject.Inject

class SignInActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var strEmail: String
    private lateinit var strPassword: String

    @Inject
    lateinit var appDatabase: AppDB

    @Inject
    lateinit var appPref: AppPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setClickListeners()
    }

    private fun init() {
        (application as BaseApplication).getApplicationComponent().inject(this)
    }

    /**
     * method for click listeners
     */
    private fun setClickListeners() {
        binding.llSignIn.setOnClickListener(this)
        binding.tvCreateAccount.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.llSignIn -> signInButtonClicked()
            R.id.tvCreateAccount -> navigateToSignUpActivity()
        }
    }

    /**
     * navigate user to signup activity if they have not created account
     */
    private fun navigateToSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    /**
     * method is called when click on signup button
     */
    private fun signInButtonClicked() {
        strEmail = binding.etEmail.text.toString().trim()
        strPassword = binding.etPassword.text.toString().trim()

        if (validateEmail(strEmail)) {
            if (validatePassword(strPassword)) {
                checkUserDataInRoomDB()
            }
        }
    }

    /**
     * method for check user data in to user table of the room database
     */
    private fun checkUserDataInRoomDB() {

        val userData = appDatabase.userDao()
            .getDataByEmailAndPassword(email = strEmail, password = strPassword)

        userData?.let {
            appPref.setValue(ALREADY_LOGGED_IN, true)
            navigateUserToHomeActivity()
        } ?: run {
            Toast.makeText(
                this,
                getString(R.string.email_or_password_is_incorrect),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * method for navigate user to home screen after user signed in in to the app
     */
    private fun navigateUserToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}