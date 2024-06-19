package com.cataract.detection.dashboard

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.cataract.detection.R
import com.cataract.detection.databinding.FragmentResultDetectionBinding
import kotlin.math.roundToInt


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

        val uriString   = arguments?.getString("image")
        val result      = arguments?.getString("result")
        var persen      = arguments?.getString("persen")
        var persenToDecimal = persen?.let { stringToDouble(it) }
        persen = persenToDecimal?.let { toPercentage(it) }

        val uri = Uri.parse(uriString)

        Glide.with(binding.imageView.context)
            .load(uri)
            .placeholder(R.drawable.preview_image)
            .error(R.drawable.preview_image)
            .into(binding.imageView)

        binding.result.text = result
        binding.persen.text = "Deteksi menunjukkan bahwa kondisi mata Anda ${result} dengan confidence rate ${persen}. confidence rate adalah tingkat kepercayaan diri alat deteksi kami terhadap hasil deteksi."
    }

    fun stringToDouble(value: String): Double {
        return value.toDouble()
    }

    fun toPercentage(value: Double): String {
        val percentage = value * 100
        return "${percentage.roundToInt()}%"
    }

}