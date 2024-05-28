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
import com.cataract.detection.connection.adapter.ListHistoryAdapter
import com.cataract.detection.connection.model.ListArticleLandscapeModel
import com.cataract.detection.connection.model.ListArticlePortraitModel
import com.cataract.detection.connection.model.ListHistoryModel
import com.cataract.detection.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val listHistoryModel = ArrayList<ListHistoryModel>()
    private lateinit var rvHistory: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvHistory = binding.rvHistory
        rvHistory.setHasFixedSize(true)

        val dataHistory = arrayOf(
            mapOf(
                "item_date"        to "Pendeteksi 12/12/2012",
                "item_result"      to "Hasil: Normal",
                "item_result_date" to "Dideteksi pada 12/12/2021, 12:00",
            ),
            mapOf(
                "item_date"        to "Pendeteksi 12/12/2012",
                "item_result"      to "Hasil: Normal",
                "item_result_date" to "Dideteksi pada 12/12/2021, 12:00",
            ),
            mapOf(
                "item_date"        to "Pendeteksi 12/12/2012",
                "item_result"      to "Hasil: Normal",
                "item_result_date" to "Dideteksi pada 12/12/2021, 12:00",
            ),
            mapOf(
                "item_date"        to "Pendeteksi 12/12/2012",
                "item_result"      to "Hasil: Normal",
                "item_result_date" to "Dideteksi pada 12/12/2021, 12:00",
            ),

        )

        insertDataHistoryToModel(dataHistory)
        showHistory()
    }

    private fun <T> insertDataHistoryToModel(listDataHistory: Array<Map<String, T>>) {
        for (data in listDataHistory) {
            val history = ListHistoryModel(data["item_date"].toString(), data["item_result"].toString(), data["item_result_date"].toString()  )
            listHistoryModel.add(history)
        }
    }

    private fun showHistory(){
        rvHistory.layoutManager = LinearLayoutManager(requireActivity())
        val listArticleAdapter = ListHistoryAdapter(listHistoryModel)
        rvHistory.adapter = listArticleAdapter

        listArticleAdapter.setOnItemClickCallback(object : ListHistoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListHistoryModel) {
                dumpHistory(data)
            }
        })
    }

    private fun dumpHistory(history: ListHistoryModel) {
        findNavController().navigate(R.id.action_historyFragment_to_detailHistoryFragment)
    }


}