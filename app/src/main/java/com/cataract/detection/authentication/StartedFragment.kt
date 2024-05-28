package com.cataract.detection.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cataract.detection.databinding.FragmentStartedBinding
import androidx.navigation.fragment.findNavController
import com.cataract.detection.R

class StartedFragment : Fragment() {

    private var _binding: FragmentStartedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.login.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        binding.register.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }

    }

}