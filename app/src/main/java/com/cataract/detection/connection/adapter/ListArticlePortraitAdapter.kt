package com.cataract.detection.connection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cataract.detection.R
import com.cataract.detection.connection.model.ArticleModel
import com.cataract.detection.databinding.CardArticlePortraitBinding

class ListArticlePortraitAdapter(private val data: ArrayList<ArticleModel>) : RecyclerView.Adapter<ListArticlePortraitAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = CardArticlePortraitBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }
    class ListViewHolder(val binding: CardArticlePortraitBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (_, photo, title, _, writer) = data[position]

        Glide.with(holder.binding.imgItemPhoto.context)
            .load(photo)
            .placeholder(R.drawable.preview_image)
            .error(R.drawable.preview_image)
            .into(holder.binding.imgItemPhoto)

        holder.binding.itemName.text = title
        holder.binding.itemWriter.text = writer
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