package com.riezki.vixnewsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.riezki.vixnewsapp.R
import com.riezki.vixnewsapp.data.local.entity.NewsEntity
import com.riezki.vixnewsapp.databinding.ListHeadlineItemBinding
import com.riezki.vixnewsapp.model.response.ArticlesItem


class NewsAdapter(private val onItemClick: (ArticlesItem) -> Unit) :
    PagingDataAdapter<ArticlesItem, NewsAdapter.NewsViewHolder>(DIFF_CALLBACK) {

    private var itemClickListener: ItemOnClickListener? = null

    fun setOnClickItemListener(itemOnClickLister: ItemOnClickListener) {
        this.itemClickListener = itemOnClickLister
    }

    class NewsViewHolder(val binding: ListHeadlineItemBinding, val onItemClick: (ArticlesItem) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(articlesItem: ArticlesItem?) {
            with(binding) {
                imgHealineList.load(articlesItem?.urlToImage) {
                    placeholder(R.drawable.ic_download_for_offline)
                    transformations(RoundedCornersTransformation(topRight = 16f, bottomRight = 16f))
                    crossfade(true)
                }
                titleHeadline.text = articlesItem?.title
                namaPenulisId.text = articlesItem?.author
                tanggalTxt.text = articlesItem?.publishedAt
            }
            itemView.setOnClickListener {
                articlesItem?.let { it1 -> onItemClick(it1) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean =
                oldItem.url == newItem.url


            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean =
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

        val ivBookmark = holder.binding.bookmark
        ivBookmark.setOnClickListener {
            itemClickListener?.onBookmarkClick(items)
        }
    }

    interface ItemOnClickListener {
        fun onBookmarkClick(articlesItem: ArticlesItem?)
    }

}