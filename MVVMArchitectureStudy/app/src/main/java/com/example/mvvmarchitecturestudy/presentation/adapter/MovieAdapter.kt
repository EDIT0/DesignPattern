package com.example.mvvmarchitecturestudy.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmarchitecturestudy.BuildConfig
import com.example.mvvmarchitecturestudy.data.model.MovieModelResult
import com.example.mvvmarchitecturestudy.databinding.MovieListItemBinding

class MovieAdapter : ListAdapter<MovieModelResult, MovieAdapter.ViewHolder>(diffUtil) {

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

        fun bind(popularMovieModelResult: MovieModelResult) {

            Glide.with(binding.ivThumbnail.context)
                .load(BuildConfig.BASE_MOVIE_POSTER + popularMovieModelResult.posterPath)
                .into(binding.ivThumbnail)

            binding.tvTitle.text = popularMovieModelResult.title
            binding.tvReleaseDate.text = popularMovieModelResult.releaseDate

            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(adapterPosition, popularMovieModelResult)
                }
            }
        }
    }

    private var onItemClickListener : ((Int, MovieModelResult) -> String) ?= null

    fun setOnItemClickListener(listener : (Int, MovieModelResult) -> String) {
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