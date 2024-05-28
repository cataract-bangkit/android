package com.cataract.detection.dashboard

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cataract.detection.R
import com.cataract.detection.databinding.FragmentResultDetectionBinding


class ResultDetectionFragment : Fragment() {
    private var _binding: FragmentResultDetectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultDetectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uriString = arguments?.getString("image")
        val uri = Uri.parse(uriString)

        binding.imageView.setImageURI(uri)
    }

}