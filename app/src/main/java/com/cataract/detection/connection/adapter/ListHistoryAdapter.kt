package com.cataract.detection.connection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cataract.detection.connection.model.ListHistoryModel
import com.cataract.detection.databinding.CardHistoryBinding

class ListHistoryAdapter(private val dataArray: ArrayList<ListHistoryModel>) : RecyclerView.Adapter<ListHistoryAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = CardHistoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }
    class ListViewHolder(val binding: CardHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (item_date, item_result, item_result_date) = dataArray[position]
        holder.binding.itemResultDate.text = item_result_date
        holder.binding.itemResult.text     = item_result
        holder.binding.itemDate.text       = item_date

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(dataArray[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = dataArray.size


    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListHistoryModel)
    }

}