package com.cataract.detection.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cataract.detection.R
import com.cataract.detection.connection.adapter.ListArticlePortraitAdapter
import com.cataract.detection.connection.model.ListArticlePortraitModel
import com.cataract.detection.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val listArticlePortraitModel  = ArrayList<ListArticlePortraitModel>()
    private lateinit var rvArticlePortrait: RecyclerView

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
        rvArticlePortrait.setHasFixedSize(true)

        binding.openDetection.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_detectionFragment)
        }

        binding.openArticle.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_articleFragment)
        }

        val dataArticlePortrait = arrayOf(
            mapOf(
                "photo" to R.drawable.preview_image,
                "title" to "Apa Itu Katarak?",
            ),
            mapOf(
                "photo" to R.drawable.preview_image,
                "title" to "Apa Itu Katarak?"
            ),
            mapOf(
                "photo" to R.drawable.preview_image,
                "title" to "Apa Itu Katarak?"
            ),
            mapOf(
                "photo" to R.drawable.preview_image,
                "title" to "Apa Itu Katarak?"
            ),
        )

        insertDataInModelPortrait(dataArticlePortrait)
        showArticlePortrait()
    }

    private fun <T> insertDataInModelPortrait(listDataPortrait: Array<Map<String, T>>) {
        for (data in listDataPortrait) {
            val article = ListArticlePortraitModel(data["photo"] as Int, data["title"].toString() )
            listArticlePortraitModel.add(article)
        }
    }

    private fun showArticlePortrait(){
        rvArticlePortrait.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        val listArticleAdapter = ListArticlePortraitAdapter(listArticlePortraitModel)
        rvArticlePortrait.adapter = listArticleAdapter

        listArticleAdapter.setOnItemClickCallback(object : ListArticlePortraitAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListArticlePortraitModel) {
                showArticlePortrait(data)
            }
        })
    }

    private fun showArticlePortrait(article: ListArticlePortraitModel) {
        // TODO
    }
}