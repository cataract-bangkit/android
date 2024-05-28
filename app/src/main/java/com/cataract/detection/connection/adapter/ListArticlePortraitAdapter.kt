package com.cataract.detection.connection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cataract.detection.connection.model.ListArticlePortraitModel
import com.cataract.detection.databinding.CardArticlePortraitBinding

class ListArticlePortraitAdapter(private val dataArray: ArrayList<ListArticlePortraitModel>) : RecyclerView.Adapter<ListArticlePortraitAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = CardArticlePortraitBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }
    class ListViewHolder(val binding: CardArticlePortraitBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (photo, description) = dataArray[position]
        holder.binding.imgItemPhoto.setImageResource(photo)
        holder.binding.itemName.text = description
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
        fun onItemClicked(data: ListArticlePortraitModel)
    }

}