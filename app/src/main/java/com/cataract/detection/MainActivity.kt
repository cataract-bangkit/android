package com.cataract.detection

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.cataract.detection.databinding.ActivityMainBinding
import androidx.navigation.findNavController
import com.bumptech.glide.load.DataSource
import com.cataract.detection.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    private fun handleDarkTheme(){
        // Todo: Lakukan sesuatu jika thema user dark
    }

    private fun handleLightTheme(){
        // Todo: Lakukan sesuatu jika thema user light
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}