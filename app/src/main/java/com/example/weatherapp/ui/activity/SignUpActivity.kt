package com.example.weatherapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.BaseApplication
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivitySignUpBinding
import com.example.weatherapp.storage.ALREADY_LOGGED_IN
import com.example.weatherapp.storage.AppPref
import com.example.weatherapp.storage.database.AppDB
import com.example.weatherapp.storage.tables.UserEntity
import com.example.weatherapp.utils.validateEmail
import com.example.weatherapp.utils.validateName
import com.example.weatherapp.utils.validatePassword
import javax.inject.Inject

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var strName: String
    private lateinit var strEmail: String
    private lateinit var strPassword: String

    @Inject
    lateinit var appDatabase: AppDB

    @Inject
    lateinit var appPref: AppPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
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
        binding.llSignUp.setOnClickListener(this)
        binding.tvAlreadyAccount.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.llSignUp -> signUpButtonClicked()
            R.id.tvAlreadyAccount -> navigateToSignInActivity()
        }
    }

    /**
     * navigate user to sign in activity if they have an account
     */
    private fun navigateToSignInActivity() {
        onBackPressedDispatcher.onBackPressed()
    }

    /**
     * method is called when click on signup button
     */
    private fun signUpButtonClicked() {
        strName = binding.etName.text.toString().trim()
        strEmail = binding.etEmail.text.toString().trim()
        strPassword = binding.etPassword.text.toString().trim()

        if (validateName(strName)) {
            if (validateEmail(strEmail)) {
                if (validatePassword(strPassword)) {
                    storeUserDataInRoomDB()
                }
            }
        }
    }

    /**
     * method for add user data in to user table of the room database
     */
    private fun storeUserDataInRoomDB() {
        val userData = appDatabase.userDao().getDataByEmail(email = strEmail)

        userData?.let {
            Toast.makeText(
                this,
                getString(R.string.user_with_this_email_is_already_exist),
                Toast.LENGTH_SHORT
            )
                .show()
        } ?: run {
            val user = UserEntity(name = strName, email = strEmail, password = strPassword)
            appDatabase.userDao().insertSingleData(user)

            appPref.setValue(ALREADY_LOGGED_IN, true)

            navigateUserToHomeActivity()
        }
    }

    /**
     * method for navigate user to home screen after user signed up in to the app
     */
    private fun navigateUserToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}