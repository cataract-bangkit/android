package com.cataract.detection.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.cataract.detection.MainActivity
import com.cataract.detection.databinding.FragmentSettingBinding
import com.cataract.detection.viewmodel.SettingViewModel

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val settingViewModel: SettingViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val switchTheme = binding.buttonTheme

        binding.buttonLogout.setOnClickListener {
            showToast("Mulai Menghapus Sesi")
            settingViewModel.logout(requireContext())
        }

        settingViewModel.messageError.observe(requireActivity(), Observer { message ->
            message?.let {
                showToast(it)
            }
        })

        settingViewModel.messageSuccess.observe(requireActivity(), Observer { message ->
            message?.let {
                showToast(it)
            }
            moveOnAuthentication()
        })

        settingViewModel.getThemeSettings(requireContext())
            .observe(requireActivity()) { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchTheme.isChecked = false
                }
            }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.setThemeSetting(requireContext(), isChecked)
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun moveOnAuthentication() {
        showToast("Berhasil Logout")

        val moveOn = Intent(requireContext(), MainActivity::class.java)
        startActivity(moveOn)
        requireActivity().finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

}