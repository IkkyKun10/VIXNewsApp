package com.riezki.vixnewsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.riezki.vixnewsapp.R
import com.riezki.vixnewsapp.databinding.ItemEverythingHeadlineBinding
import com.riezki.vixnewsapp.model.response.ArticlesItem

class ItemEverythingHeadlineAdapter(private val onClick: (ArticlesItem) -> Unit)
    : ListAdapter<ArticlesItem, ItemEverythingHeadlineAdapter.ItemEverythingViewHolder>
    (DIFF_CALLBACK) {

    companion object {
            private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
                override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean =
                    oldItem.url == newItem.url


                override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean =
                    oldItem == newItem

            }
        }

    class ItemEverythingViewHolder(private val binding: ItemEverythingHeadlineBinding, val onClick: (ArticlesItem) -> Unit)
        : RecyclerView.ViewHolder(binding.root) {

            fun bind(item: ArticlesItem) {
                with(binding) {
                    titleHeadlineTxt.text = item.title
                    mediaPublishTxt.text = item.source?.name
                    tanggalPublishTxt.text = item.publishedAt
                    imgHeadline.load(item.urlToImage){
                        crossfade(true)
                        placeholder(R.drawable.ic_download_for_offline)
                        transformations(RoundedCornersTransformation(20f, 20f, 20f, 20f))
                    }
                }
                itemView.setOnClickListener {
                    onClick(item)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemEverythingViewHolder {
        val view = ItemEverythingHeadlineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemEverythingViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ItemEverythingViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}