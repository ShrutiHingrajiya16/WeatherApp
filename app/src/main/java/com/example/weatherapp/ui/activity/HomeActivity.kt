package com.example.weatherapp.ui.activity


import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.BaseApplication
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityHomeBinding
import com.example.weatherapp.retrofit.ApiInterface
import com.example.weatherapp.storage.AppPref
import com.example.weatherapp.storage.database.AppDB
import com.example.weatherapp.ui.adapter.WeatherTabLayoutAdapter
import com.example.weatherapp.ui.viewmodel.HomeViewModel
import com.example.weatherapp.utils.REQUEST_CODE_LOCATION
import com.example.weatherapp.utils.REQUEST_CODE_LOCATION_PERMISSION
import com.example.weatherapp.utils.isNetworkAvailable
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import java.util.Locale
import javax.inject.Inject


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    @Inject
    lateinit var appDB: AppDB

    @Inject
    lateinit var appPref: AppPref

    @Inject
    lateinit var apiInterface: ApiInterface

    private val LOCATION_PERMISSION = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private var latitude = 0.0
    private var longitude = 0.0
    private var countryName = ""
    private var cityName = ""
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setClickListeners()
    }

    private fun init() {
        (application as BaseApplication).getApplicationComponent().inject(this)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        if (isNetworkAvailable()) {
            getLocationPermission()
        } else {
            Toast.makeText(
                this,
                getString(R.string.check_your_internet_connectivity),
                Toast.LENGTH_SHORT
            ).show()
        }
        setBottomTabLayout()
        setViewPagerAdapter()
    }

    /**
     * set viewpager according to tabs
     */
    private fun setViewPagerAdapter() {
        val adapter = WeatherTabLayoutAdapter(
            this,
            supportFragmentManager,
            binding.tbWeather.tabCount,
            lifecycle
        )
        binding.vpWeather.adapter = adapter
        binding.vpWeather.isUserInputEnabled = false

    }

    /**
     * set bottom tabs with
     * 1. Current weather
     * 2. 5 days weather
     */
    private fun setBottomTabLayout() {
        binding.tbWeather.addTab(binding.tbWeather.newTab().setText(getString(R.string.current)))
        binding.tbWeather.addTab(binding.tbWeather.newTab().setText(getString(R.string._5_days)))
        binding.tbWeather.tabGravity = TabLayout.GRAVITY_FILL
    }

    /**
     * method for check and get location permission
     */
    private fun getLocationPermission() {
        if (checkPermission()) {
            getCurrentLocationLatLng()
        } else {
            permissionRequest.launch(LOCATION_PERMISSION)
        }
    }

    private fun getCurrentLocationLatLng() {
        if (isLocationEnabled()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                getLocationPermission()
            } else {
                val locationRequest = LocationRequest.create() // Create location request.
                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY // Set priority.

                val locationCallback: LocationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        for (location in locationResult.locations) {
                            if (location != null) {
                                val geocoder = Geocoder(this@HomeActivity, Locale.getDefault())
                                val list: List<Address> =
                                    geocoder.getFromLocation(
                                        location.latitude,
                                        location.longitude,
                                        1
                                    ) as List<Address>

                                latitude = list[0].latitude
                                longitude = list[0].longitude
                                countryName = list[0].countryName
                                cityName = list[0].locality

                                viewModel.setCityCountry(cityName, countryName)
                                getCurrentWeather()
                                getWeatherList()
                            }
                        }
                    }
                }
                val client = LocationServices.getFusedLocationProviderClient(this)
                Looper.myLooper()
                    ?.let { client.requestLocationUpdates(locationRequest, locationCallback, it) }
            }
        } else {
            Toast.makeText(this, getString(R.string.please_turn_on_location), Toast.LENGTH_LONG)
                .show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(intent, REQUEST_CODE_LOCATION)
        }

    }

    /**
     * method for checking location is enable or disable
     */
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }

            if (granted) {
                //Toast.makeText(this, "Granted Permission", Toast.LENGTH_SHORT).show()
                getLocationPermission()
            } else {
                //Toast.makeText(this, "Denied Permission", Toast.LENGTH_SHORT).show()
                showDialogForLocation()
            }
        }

    /**
     * show dialog for location permission
     */
    private fun showDialogForLocation() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(getString(R.string.location_permission))
        dialog.setMessage(getString(R.string.you_have_to_allow_location_permission_for_getting_weather_data))

        dialog.setPositiveButton(getString(R.string.go_to_setting)) { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.cancel()
            gotoAppSettingScreen()
        }

        dialog.setNegativeButton(getString(R.string.cancel)) { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.cancel()
            finish()
        }
        dialog.show()
    }

    private fun gotoAppSettingScreen() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, REQUEST_CODE_LOCATION_PERMISSION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            getLocationPermission()
        }
        if (requestCode == REQUEST_CODE_LOCATION) {
            getCurrentLocationLatLng()
        }
    }

    private fun checkPermission(): Boolean {
        for (permission in LOCATION_PERMISSION) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private fun getWeatherList() {
        viewModel.requestForDaysWeatherData(
            latitude,
            longitude,
            getString(R.string.api_key),
            "metric",
            apiInterface
        )
    }

    private fun getCurrentWeather() {

        viewModel.requestForCurrentWeatherData(
            latitude,
            longitude,
            getString(R.string.api_key),
            "metric",
            apiInterface
        )
    }


    /**
     * method for click listeners
     */
    private fun setClickListeners() {
        binding.tbWeather.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.vpWeather.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.ivLogOut.setOnClickListener {
            logoutClick()
        }
    }

    /**
     * method is called when logout click called
     */
    private fun logoutClick() {
        viewModel.logout(appDB, appPref)
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}