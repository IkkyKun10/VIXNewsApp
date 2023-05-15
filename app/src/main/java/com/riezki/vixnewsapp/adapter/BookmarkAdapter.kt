package com.riezki.vixnewsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.riezki.vixnewsapp.R
import com.riezki.vixnewsapp.data.local.entity.NewsEntity
import com.riezki.vixnewsapp.databinding.ListHeadlineItemBinding


class BookmarkAdapter(private val onItemClick: (NewsEntity) -> Unit) : ListAdapter<NewsEntity, BookmarkAdapter.NewsViewHolder>(DIFF_CALLBACK) {

    class NewsViewHolder(private val binding: ListHeadlineItemBinding, val onItemClick: (NewsEntity) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        fun bind(articlesItem: NewsEntity) {
            with(binding) {
                imgHealineList.load(articlesItem.urlToImage){
                    placeholder(R.drawable.ic_download_for_offline)
                    transformations(RoundedCornersTransformation())
                    crossfade(true)
                }
                titleHeadline.text = articlesItem.title
                namaPenulisId.text = articlesItem.author
                tanggalTxt.text = articlesItem.publishedAt
            }
            itemView.setOnClickListener {
                onItemClick(articlesItem)
            }
        }
    }

    companion object {
            private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsEntity>() {
                override fun areItemsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean =
                    oldItem.url == newItem.url
    
    
                override fun areContentsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean =
                    oldItem == newItem
    
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = ListHeadlineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val items = getItem(position)
        holder.bind(items)
    }

}