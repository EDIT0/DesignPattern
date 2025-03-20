package com.my.mvistudymultimodule.feature.xml.mainhome.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.my.mvistudymultimodule.core.di.BuildConfig
import com.my.mvistudymultimodule.core.model.MovieModel
import com.my.mvistudymultimodule.core.util.ClickUtil.onSingleClick
import com.my.mvistudymultimodule.core.util.ClickUtil.onSingleClickWithDebounce
import com.my.mvistudymultimodule.feature.xml.databinding.ItemMovieBinding
import kotlinx.coroutines.flow.launchIn

class MovieListPagingAdapter : PagingDataAdapter<MovieModel.MovieModelResult, MovieListPagingAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: MovieModel.MovieModelResult) {

            binding.executePendingBindings()

            Glide.with(binding.ivThumbnail.context)
                .load(BuildConfig.BASE_MOVIE_POSTER + data.posterPath)
                .error(com.my.mvistudymultimodule.core.base.R.drawable.ic_search_24_000000)
                .into(binding.ivThumbnail)

            binding.tvTitle.text = data.title
            binding.tvReleaseDate.text = data.releaseDate

//            binding.root.setOnClickListener {
//                onItemClickListener?.let {
//                    it(adapterPosition, data)
//                }
//            }
            binding.root.onSingleClick(interval = 500L, action = {
                onItemClickListener?.let {
                    it(adapterPosition, data)
                }
            })
        }
    }

    private var onItemClickListener : ((Int, MovieModel.MovieModelResult) -> Unit)? = null

    fun setOnItemClickListener(listener : (Int, MovieModel.MovieModelResult) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        val diffUtil = object: DiffUtil.ItemCallback<MovieModel.MovieModelResult>() {
            override fun areContentsTheSame(oldItem: MovieModel.MovieModelResult, newItem: MovieModel.MovieModelResult) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: MovieModel.MovieModelResult, newItem: MovieModel.MovieModelResult) =
                oldItem.title == newItem.title
        }
    }

}