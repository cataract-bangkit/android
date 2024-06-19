package com.cataract.detection.connection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cataract.detection.R
import com.cataract.detection.connection.model.ArticleModel
import com.cataract.detection.databinding.CardArticleLandscapeBinding

class ListArticleLandscapeAdapter(private val data: ArrayList<ArticleModel>) : RecyclerView.Adapter<ListArticleLandscapeAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = CardArticleLandscapeBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }
    class ListViewHolder(val binding: CardArticleLandscapeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (_, photo, title, description, writer) = data[position]

        Glide.with(holder.binding.imgItemPhoto.context)
            .load(photo)
            .placeholder(R.drawable.preview_image)
            .error(R.drawable.preview_image)
            .into(holder.binding.imgItemPhoto)

        holder.binding.itemName.text = title
        holder.binding.itemWriter.text = writer
        holder.binding.itemDescription.text = description
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(data[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = data.size


    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ArticleModel)
    }
}