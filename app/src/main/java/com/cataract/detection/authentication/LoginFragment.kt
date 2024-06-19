package com.cataract.detection.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cataract.detection.DashboardActivity
import com.cataract.detection.R
import com.cataract.detection.databinding.FragmentLoginBinding
import com.cataract.detection.viewmodel.LoginViewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

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
            loginViewModel.login(
                requireContext(),
                binding.inputEmail.text,
                binding.inputPhone.text,
                binding.inputPassword.text
            )
        }

        loginViewModel.messageError.observe(viewLifecycleOwner, Observer{ message ->
            message?.let {
                showToast(it)
            }
        })

        loginViewModel.messageSuccess.observe(viewLifecycleOwner, Observer{ message ->
            message?.let {
                binding.alertSuccess.visibility = View.VISIBLE
            }
        })

        loginViewModel.isLoading.observe(viewLifecycleOwner, Observer{
            showLoading(it)
        })

        binding.btnOk.setOnClickListener {
            binding.alertSuccess.visibility = View.GONE
            val moveOn = Intent(requireContext(), DashboardActivity::class.java)
            startActivity(moveOn)
            requireActivity().finishAffinity()
        }

        binding.redirectToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        _binding?.let {
            it.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}