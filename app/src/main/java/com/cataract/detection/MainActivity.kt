package com.cataract.detection

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cataract.detection.databinding.ActivityMainBinding
import androidx.navigation.findNavController
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Handler(Looper.getMainLooper()).postDelayed({
            val moveOn = Intent(this@MainActivity, AuthenticationActivity::class.java)
            startActivity(moveOn)
            finishAffinity()
        }, 5000)
    }

    private fun handleNoNetwork(){
        // Todo: Lakukan sesuatu jika jaringan tidak tersedia
    }

    private fun handleNetworkAvailable(){
        // Todo: Lakukan sesuatu jika jaringan tersedia
    }

    private fun handleUserNotLoggedIn(){
        // Todo: Lakukan sesuatu jika user tidak berhasil login
    }

    private fun handleUserLoggedIn(){
        // Todo: Lakukan sesuatu jika user berhasil login
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