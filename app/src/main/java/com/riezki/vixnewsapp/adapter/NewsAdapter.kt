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


class NewsAdapter(private val onItemClick: (NewsEntity) -> Unit) :
    PagingDataAdapter<NewsEntity, NewsAdapter.NewsViewHolder>(DIFF_CALLBACK) {

    private var itemClickListener: ItemOnClickListener? = null

    fun setOnClickItemListener(itemOnClickLister: ItemOnClickListener) {
        this.itemClickListener = itemOnClickLister
    }

    class NewsViewHolder(val binding: ListHeadlineItemBinding, val onItemClick: (NewsEntity) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(articlesItem: NewsEntity?) {
            with(binding) {
                imgHealineList.load(articlesItem?.urlToImage) {
                    placeholder(R.drawable.ic_download_for_offline)
                    //transformations(RoundedCornersTransformation(topRight = 16f, bottomRight = 16f))
                    error((R.drawable.ic_error))
                    crossfade(true)
                }
                titleHeadline.text = articlesItem?.title
                namaPenulisId.text = articlesItem?.author
                tanggalTxt.text = articlesItem?.publishedAt

//                if (articlesItem?.isBookmarked == true) {
//                    binding.bookmark.setImageDrawable(ContextCompat.getDrawable(binding.bookmark.context, R.drawable.ic_bookmark))
//                } else {
//                    binding.bookmark.setImageDrawable(ContextCompat.getDrawable(binding.bookmark.context, R.drawable.ic_bookmark_border))
//                }
            }
            itemView.setOnClickListener {
                articlesItem?.let { it1 -> onItemClick(it1) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsEntity>() {
            override fun areItemsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean =
                oldItem.title == newItem.title


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

//        val ivBookmark = holder.binding.bookmark
//        ivBookmark.setOnClickListener {
//            itemClickListener?.onBookmarkClick(items)
//        }
//
//        if (items?.isBookmarked == true) {
//            ivBookmark.setImageDrawable(ContextCompat.getDrawable(ivBookmark.context, R.drawable.ic_bookmark))
//        } else {
//            ivBookmark.setImageDrawable(ContextCompat.getDrawable(ivBookmark.context, R.drawable.ic_bookmark_border))
//        }
    }

    interface ItemOnClickListener {
        fun onBookmarkClick(newsEntity: NewsEntity?)
    }

}