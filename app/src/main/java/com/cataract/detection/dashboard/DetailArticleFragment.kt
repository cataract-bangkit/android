package com.cataract.detection.dashboard

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.cataract.detection.R
import com.cataract.detection.connection.model.ArticleModel
import com.cataract.detection.databinding.FragmentDetailArticleBinding

class DetailArticleFragment : Fragment() {
    private var _binding: FragmentDetailArticleBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val article = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getParcelable("article", ArticleModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("article")
        }

        article?.let {
            Glide.with(binding.imageView.context)
                .load(it.photo)
                .placeholder(R.drawable.preview_image)
                .error(R.drawable.preview_image)
                .into(binding.imageView)
            binding.title.text = it.title
            binding.writer.text = it.writer
            binding.description.text = it.description
        }
    }

}