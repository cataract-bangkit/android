package com.cataract.detection.connection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cataract.detection.connection.model.ListHistoryModel
import com.cataract.detection.databinding.CardHistoryBinding
import kotlin.math.roundToInt

class ListHistoryAdapter(private val dataArray: ArrayList<ListHistoryModel>) : RecyclerView.Adapter<ListHistoryAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = CardHistoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }
    class ListViewHolder(val binding: CardHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (item_id, item_result, item_img, item_confidence, item_predictedAt) = dataArray[position]

        var persenToDecimal = item_confidence?.let { stringToDouble(it) }
        var persen = persenToDecimal?.let { toPercentage(it) }


        holder.binding.itemDate.text            = item_predictedAt
        holder.binding.itemResult.text          = item_result
        holder.binding.itemPersentance.text     = persen


        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(dataArray[holder.adapterPosition])
        }
    }

    fun stringToDouble(value: String): Double {
        return value.toDouble()
    }

    fun toPercentage(value: Double): String {
        val percentage = value * 100
        return "${percentage.roundToInt()}%"
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