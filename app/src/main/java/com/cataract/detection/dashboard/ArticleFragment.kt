package com.cataract.detection.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cataract.detection.R
import com.cataract.detection.connection.adapter.ListArticleLandscapeAdapter
import com.cataract.detection.connection.adapter.ListArticlePortraitAdapter
import com.cataract.detection.connection.model.ArticleModel
import com.cataract.detection.databinding.FragmentArticleBinding
import com.cataract.detection.viewmodel.ArticleViewModel
import com.cataract.detection.viewmodel.ViewModelFactory

class ArticleFragment : Fragment() {
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private var articlePortrait = ArrayList<ArticleModel>()
    private var articleLandscape = ArrayList<ArticleModel>()

    private lateinit var rvArticlePortrait: RecyclerView
    private lateinit var rvArticleLandscape: RecyclerView

    private val articleViewModel by viewModels<ArticleViewModel> {
        ViewModelFactory.getInstance((activity as AppCompatActivity).application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvArticlePortrait = binding.rvArticlePortrait
        rvArticleLandscape = binding.rvArticleLandscape

        articleViewModel.getPortraitArticles().observe(viewLifecycleOwner) {
            articlePortrait.clear()
            articlePortrait.addAll(it)
            showArticlePortrait()
        }

        articleViewModel.getLandscapeArticles().observe(viewLifecycleOwner) {
            articleLandscape.clear()
            articleLandscape.addAll(it)
            showArticleLandscape()
        }

        articleViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }
    }

    private fun showArticlePortrait(){
        val listArticleAdapter = ListArticlePortraitAdapter(articlePortrait)
        rvArticlePortrait.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        rvArticlePortrait.adapter = listArticleAdapter

        listArticleAdapter.setOnItemClickCallback(object : ListArticlePortraitAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ArticleModel) {
                showArticlePortrait(data)
            }
        })
    }

    private fun showArticleLandscape(){
        val listArticleAdapter = ListArticleLandscapeAdapter(articleLandscape)
        rvArticleLandscape.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        rvArticleLandscape.adapter = listArticleAdapter

        listArticleAdapter.setOnItemClickCallback(object : ListArticleLandscapeAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ArticleModel) {
                showArticleLandscape(data)
            }
        })
    }

    private fun showArticlePortrait(data: ArticleModel) {
        findNavController().navigate(R.id.action_articleFragment_to_detailArticleFragment)
    }

    private fun showArticleLandscape(data: ArticleModel) {
        findNavController().navigate(R.id.action_articleFragment_to_detailArticleFragment)
    }

    private fun showLoading(isLoading: Boolean) {
        _binding?.let {
            it.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}