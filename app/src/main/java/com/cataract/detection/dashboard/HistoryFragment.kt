package com.cataract.detection.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cataract.detection.R
import com.cataract.detection.connection.adapter.ListHistoryAdapter
import com.cataract.detection.connection.model.HistoryModel
import com.cataract.detection.connection.model.ListHistoryModel
import com.cataract.detection.databinding.FragmentHistoryBinding
import com.cataract.detection.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val listHistoryModel = ArrayList<ListHistoryModel>()
    private lateinit var rvHistory: RecyclerView

    private val historyViewModel: HistoryViewModel by viewModels()

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

        historyViewModel.messageError.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                showToast(it)
            }
        })

        historyViewModel.messageSuccess.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                showToast(it)
            }
        })

        historyViewModel.listHistory.observe(viewLifecycleOwner, Observer { listHistory ->
            insertDataHistoryToModel(listHistory)
            checkSize(listHistory)
            showHistory()
        })

        if (listHistoryModel.isEmpty()) {
            historyViewModel.getHistory(requireContext())
        }

        historyViewModel.isLoading.observe(requireActivity(), Observer{
            showLoading(it)
        })
    }

    private fun insertDataHistoryToModel(listDataHistory: List<HistoryModel.HistoryItem>) {
        listHistoryModel.clear()  // Clear the existing data to avoid duplicates
        for (data in listDataHistory) {
            val history = ListHistoryModel(
                data.id.toString(),
                data.result.toString(),
                data.imgUrl.toString(),
                data.confidence.toString(),
                data.predictedAt.toString()
            )
            listHistoryModel.add(history)
        }
    }

    private fun showHistory() {
        rvHistory.layoutManager = LinearLayoutManager(requireActivity())
        val listHistoryAdapter = ListHistoryAdapter(listHistoryModel)
        rvHistory.adapter = listHistoryAdapter

        listHistoryAdapter.setOnItemClickCallback(object : ListHistoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListHistoryModel) {
                dumpHistory(data)
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun dumpHistory(history: ListHistoryModel) {
        val bundle = Bundle()

        bundle.putString("image", history.item_img)
        bundle.putString("result", history.item_result)
        bundle.putString("persen", history.item_confidence)

        findNavController().navigate(R.id.action_historyFragment_to_detailHistoryFragment, bundle)
    }

    private fun showLoading(isLoading: Boolean) {
        _binding?.let {
            it.noData.visibility = View.GONE
            it.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun checkSize(listDataHistory: List<HistoryModel.HistoryItem>) {
        _binding?.let {
            if (listDataHistory.isEmpty()) {
                it.noData.visibility = View.VISIBLE
                it.openDetection.setOnClickListener {
                    findNavController().navigate(R.id.action_historyFragment_to_detectionFragment)
                }
            } else {
                it.noData.visibility = View.GONE
            }
        }
    }
}
