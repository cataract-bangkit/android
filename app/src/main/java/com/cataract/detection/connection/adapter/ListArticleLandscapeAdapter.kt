package com.cataract.detection.connection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cataract.detection.connection.model.ListArticleLandscapeModel
import com.cataract.detection.databinding.CardArticleLandscapeBinding

class ListArticleLandscapeAdapter(private val dataArray: ArrayList<ListArticleLandscapeModel>) : RecyclerView.Adapter<ListArticleLandscapeAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = CardArticleLandscapeBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }
    class ListViewHolder(val binding: CardArticleLandscapeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (photo, title, description) = dataArray[position]
        holder.binding.imgItemPhoto.setImageResource(photo)
        holder.binding.itemName.text = title
        holder.binding.itemDescription.text = description
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
        fun onItemClicked(data: ListArticleLandscapeModel)
    }

}