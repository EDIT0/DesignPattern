package com.my.mvistudymultimodule.feature.xml.mainhome.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.my.mvistudymultimodule.core.base.R
import com.my.mvistudymultimodule.feature.xml.databinding.ItemLoadingStateBinding

class LoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<com.my.mvistudymultimodule.feature.xml.mainhome.view.adapter.LoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = ItemLoadingStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }

    inner class LoadStateViewHolder(
        private val binding: ItemLoadingStateBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            // 버튼 클릭 시 Fragment 에서 받아온 동작 호출 (retry)
            binding.btnError.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState : LoadState){
            if(loadState is LoadState.Error) {
                binding.tvError.text = loadState.error.localizedMessage
            } else {
                binding.tvError.text = binding.root.context.getString(R.string.common_list_retry)
            }

            binding.apply {
                progressbar.isVisible = loadState is LoadState.Loading
                btnError.isVisible = loadState is LoadState.Error
                tvError.isVisible = loadState is LoadState.Error
            }
        }
    }


}