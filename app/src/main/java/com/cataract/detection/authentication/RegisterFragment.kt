package com.cataract.detection.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cataract.detection.R
import com.cataract.detection.databinding.FragmentRegisterBinding
import com.cataract.detection.viewmodel.RegisterViewModel

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val registerViewModel: RegisterViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRegister.setOnClickListener {
            registerViewModel.registerUser(
                requireContext(),
                binding.inputName.text,
                binding.inputEmail.text,
                binding.inputPhone.text,
                binding.inputPassword.text
            )
        }

        registerViewModel.messageError.observe(requireActivity(), Observer{ message ->
            message?.let {
                showToast(it)
            }
        })

        registerViewModel.messageSuccess.observe(requireActivity(), Observer{ message ->
            message?.let {
                binding.alertSuccess.visibility = View.VISIBLE
            }
        })

        binding.btnOk.setOnClickListener {
            binding.alertSuccess.visibility = View.GONE
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            showToast("Akun Berhasil Dibuat Silahkan Login")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }


}