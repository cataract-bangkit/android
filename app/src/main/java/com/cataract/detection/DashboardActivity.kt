package com.cataract.detection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.cataract.detection.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private var _binding: ActivityDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.navhost_fragmentdashboard) as NavHostFragment

        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    navHostFragment.findNavController().navigate(R.id.homeFragment)
                    true
                }
                R.id.nav_artikel -> {
                    navHostFragment.findNavController().navigate(R.id.articleFragment)
                    true
                }
                R.id.nav_deteksi -> {
                    navHostFragment.findNavController().navigate(R.id.detectionFragment)
                    true
                }
                R.id.nav_riwayat -> {
                    navHostFragment.findNavController().navigate(R.id.historyFragment)
                    true
                }
                R.id.nav_setting -> {
                    navHostFragment.findNavController().navigate(R.id.settingFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}