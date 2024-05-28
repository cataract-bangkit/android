package com.cataract.detection.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cataract.detection.DashboardActivity
import com.cataract.detection.R
import com.cataract.detection.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener {
            binding.alertSuccess.visibility = View.VISIBLE
        }

        binding.btnOk.setOnClickListener {
            binding.alertSuccess.visibility = View.GONE
            val moveOn = Intent(requireContext(), DashboardActivity::class.java)
            startActivity(moveOn)
            requireActivity().finishAffinity()
        }
    }

}