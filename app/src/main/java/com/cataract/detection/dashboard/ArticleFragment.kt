package com.cataract.detection.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cataract.detection.R
import com.cataract.detection.connection.adapter.ListArticleLandscapeAdapter
import com.cataract.detection.connection.adapter.ListArticlePortraitAdapter
import com.cataract.detection.connection.model.ListArticleLandscapeModel
import com.cataract.detection.connection.model.ListArticlePortraitModel
import com.cataract.detection.databinding.FragmentArticleBinding

class ArticleFragment : Fragment() {
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private val listArticleLandscapeModel = ArrayList<ListArticleLandscapeModel>()
    private val listArticlePortraitModel  = ArrayList<ListArticlePortraitModel>()
    private lateinit var rvArticlePortrait: RecyclerView
    private lateinit var rvArticleLandscape: RecyclerView


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
        rvArticlePortrait.setHasFixedSize(true)

        rvArticleLandscape = binding.rvArticleLandscape
        rvArticleLandscape.setHasFixedSize(true)

        val dataArticlePortrait = arrayOf(
            mapOf(
                "photo" to R.drawable.ic_launcher_background,
                "title" to "Apa Itu Katarak",
            ),
            mapOf(
                "photo" to R.drawable.ic_launcher_background,
                "title" to "Apa Itu Katarak"
            ),
            mapOf(
                "photo" to R.drawable.ic_launcher_background,
                "title" to "Apa Itu Katarak"
            ),
            mapOf(
                "photo" to R.drawable.ic_launcher_background,
                "title" to "Apa Itu Katarak"
            ),
        )

        val dataArticleLandscape = arrayOf(
            mapOf(
                "photo" to R.drawable.ic_launcher_background,
                "title" to "Apa Itu Katarak",
                "description" to "Katarak adalah suatu penyakit ketika lensa mata menjadi keruh dan berawan."
            ),
            mapOf(
                "photo" to R.drawable.ic_launcher_background,
                "title" to "Apa Itu Katarak",
                "description" to "Katarak adalah suatu penyakit ketika lensa mata menjadi keruh dan berawan."
            ),
            mapOf(
                "photo" to R.drawable.ic_launcher_background,
                "title" to "Apa Itu Katarak",
                "description" to "Katarak adalah suatu penyakit ketika lensa mata menjadi keruh dan berawan."
            ),
            mapOf(
                "photo" to R.drawable.ic_launcher_background,
                "title" to "Apa Itu Katarak",
                "description" to "Katarak adalah suatu penyakit ketika lensa mata menjadi keruh dan berawan."
            ),
        )

        insertDataInModelLandscape(dataArticleLandscape)
        insertDataInModelPortrait(dataArticlePortrait)
        showArticlePortrait()
        showArticleLandscape()
    }

    private fun <T> insertDataInModelPortrait(listDataPortrait: Array<Map<String, T>>) {
        for (data in listDataPortrait) {
            val article = ListArticlePortraitModel(data["photo"] as Int, data["title"].toString() )
            listArticlePortraitModel.add(article)
        }
    }

    private fun <T> insertDataInModelLandscape(listDataLandscape: Array<Map<String, T>>) {
        for (data in listDataLandscape) {
            val article = ListArticleLandscapeModel(data["photo"] as Int, data["title"].toString(), data["description"].toString() )
            listArticleLandscapeModel.add(article)
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

    private fun showArticleLandscape(){
        rvArticleLandscape.layoutManager = LinearLayoutManager(requireActivity())
        val listArticleAdapter = ListArticleLandscapeAdapter(listArticleLandscapeModel)
        rvArticleLandscape.adapter = listArticleAdapter

        listArticleAdapter.setOnItemClickCallback(object : ListArticleLandscapeAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListArticleLandscapeModel) {
                showArticleLandscape(data)
            }
        })
    }

    private fun showArticlePortrait(article: ListArticlePortraitModel) {
        findNavController().navigate(R.id.action_articleFragment_to_detailArticleFragment)
    }

    private fun showArticleLandscape(article: ListArticleLandscapeModel) {
        findNavController().navigate(R.id.action_articleFragment_to_detailArticleFragment)
    }

}