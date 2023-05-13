package com.riezki.vixnewsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.riezki.vixnewsapp.databinding.ItemLoadingWithErrorBinding

class ItemLoadingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<ItemLoadingStateAdapter.LoadingStateViewHolder>() {

    class LoadingStateViewHolder(private val binding: ItemLoadingWithErrorBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }
        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMsg.isVisible = loadState is LoadState.Error
        }
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingStateViewHolder {
        val view = ItemLoadingWithErrorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(view, retry)
    }
}