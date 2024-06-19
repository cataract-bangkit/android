package com.cataract.detection.connection.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cataract.detection.connection.model.ListHistoryModel
import com.cataract.detection.databinding.CardHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

class ListHistoryAdapter(private val dataArray: ArrayList<ListHistoryModel>) : RecyclerView.Adapter<ListHistoryAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = CardHistoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }
    class ListViewHolder(val binding: CardHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (_, item_result, _, item_confidence, item_predictedAt) = dataArray[position]

        var persenToDecimal = item_confidence?.let { stringToDouble(it) }
        var persen = persenToDecimal?.let { toPercentage(it) }


        holder.binding.itemDate.text            = item_predictedAt?.withDateFormat()
        holder.binding.itemResult.text          = item_result
        holder.binding.itemPersentance.text     = persen


        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(dataArray[holder.adapterPosition])
        }
    }

    private fun stringToDouble(value: String): Double {
        return value.toDouble()
    }

    private fun toPercentage(value: Double): String {
        val percentage = value * 100
        return "${percentage.roundToInt()}%"
    }

    private fun String.withDateFormat(): String? {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")

            val date = inputFormat.parse(this) as Date
            val outputFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("in", "ID"))

            outputFormat.format(date)

        } catch (e: Exception) {
            Log.e("ListHistoryAdapter", e.toString())
            this
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