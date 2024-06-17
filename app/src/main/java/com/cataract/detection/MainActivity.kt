package com.cataract.detection

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import com.cataract.detection.databinding.ActivityMainBinding
import com.cataract.detection.viewmodel.MainViewModel
import com.cataract.detection.viewmodel.SettingViewModel

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleTheme()

        mainViewModel.loggedIn.observe(this, Observer { setupComplete ->
            if (setupComplete) {
                handleUserLoggedIn()
            } else {
                _binding = ActivityMainBinding.inflate(layoutInflater)
                val view = binding.root
                setContentView(view)

                handleUserNotLoggedIn()
            }
        })

        mainViewModel.isUserLoggedIn(this)

    }

    private fun handleUserNotLoggedIn(){
        val moveOn = Intent(this@MainActivity, AuthenticationActivity::class.java)
        startActivity(moveOn)
        finish()
    }

    private fun handleUserLoggedIn(){
        val moveOn = Intent(this@MainActivity, DashboardActivity::class.java)
        startActivity(moveOn)
        finish()
    }

    private fun handleTheme(){
        settingViewModel.getThemeSettings(this).observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}