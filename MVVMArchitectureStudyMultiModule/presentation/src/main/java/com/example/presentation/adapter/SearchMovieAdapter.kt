package com.example.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.data.BuildConfig
import com.example.domain.model.MovieModelResult
import com.example.presentation.databinding.MovieListItemBinding

class SearchMovieAdapter : ListAdapter<MovieModelResult, SearchMovieAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun replaceItems(items: List<MovieModelResult?>) {
        submitList(items)
    }


    inner class ViewHolder(val binding: MovieListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(searchMovieModelResult: MovieModelResult) {

            Glide.with(binding.ivThumbnail.context)
                .load(BuildConfig.BASE_MOVIE_POSTER + searchMovieModelResult.posterPath)
                .into(binding.ivThumbnail)

            binding.tvTitle.text = searchMovieModelResult.title
            binding.tvReleaseDate.text = searchMovieModelResult.releaseDate

            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(searchMovieModelResult)
                }
            }
        }
    }

    private var onItemClickListener : ((MovieModelResult) -> Unit) ?= null

    fun setOnItemClickListener(listener : (MovieModelResult) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        val diffUtil = object: DiffUtil.ItemCallback<MovieModelResult>() {
            override fun areContentsTheSame(oldItem: MovieModelResult, newItem: MovieModelResult) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: MovieModelResult, newItem: MovieModelResult) =
                oldItem.id == newItem.id
        }
    }

}