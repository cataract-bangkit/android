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
import com.cataract.detection.connection.adapter.ListArticlePortraitAdapter
import com.cataract.detection.connection.model.ArticleModel
import com.cataract.detection.connection.service.PreferencesService
import com.cataract.detection.databinding.FragmentHomeBinding
import com.cataract.detection.viewmodel.HomeViewModel
import com.cataract.detection.viewmodel.ViewModelFactory

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var articlePortrait = ArrayList<ArticleModel>()
    private lateinit var rvArticlePortrait: RecyclerView
    private lateinit var preferencesService: PreferencesService

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance((activity as AppCompatActivity).application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvArticlePortrait = binding.rvArticlePortrait
        preferencesService = PreferencesService(requireContext())
        val userName = preferencesService.getName()

        binding.textView.setText("Hello, ${userName}")

        binding.openDetection.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_detectionFragment)
        }

        binding.openArticle.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_articleFragment)
        }

        homeViewModel.getPortraitArticles().observe(viewLifecycleOwner) {
            articlePortrait.clear()
            articlePortrait.addAll(it)
            showArticlePortrait()
        }
    }

    private fun showArticlePortrait(){
        val listArticleAdapter = ListArticlePortraitAdapter(articlePortrait)
        rvArticlePortrait.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        rvArticlePortrait.adapter = listArticleAdapter

        listArticleAdapter.setOnItemClickCallback(object : ListArticlePortraitAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ArticleModel) {
                showArticle(data)
            }
        })
    }

    private fun showArticle(data: ArticleModel) {
        val bundle = Bundle()
        bundle.putParcelable("article", data)
        findNavController().navigate(R.id.action_homeFragment_to_detailArticleFragment, bundle)
    }
}