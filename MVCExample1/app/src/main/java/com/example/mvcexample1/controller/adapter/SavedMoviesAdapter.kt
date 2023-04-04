package com.example.mvcexample1.controller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvcexample1.BuildConfig
import com.example.mvcexample1.databinding.MovieListItemBinding
import com.example.mvcexample1.model.data.MovieModel

class SavedMoviesAdapter : ListAdapter<MovieModel.MovieModelResult, SavedMoviesAdapter.ViewHolder>(diffUtil){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun replaceItems(items: List<MovieModel.MovieModelResult?>) {
        submitList(items)
    }

    inner class ViewHolder(val binding: MovieListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(savedMoviesModelResult: MovieModel.MovieModelResult) {

            Glide.with(binding.ivThumbnail.context)
                .load(BuildConfig.BASE_MOVIE_POSTER + savedMoviesModelResult.posterPath)
                .into(binding.ivThumbnail)

            binding.tvTitle.text = savedMoviesModelResult.title
            binding.tvReleaseDate.text = savedMoviesModelResult.releaseDate

            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(savedMoviesModelResult)
                }
            }
        }
    }

    private var onItemClickListener : ((MovieModel.MovieModelResult) -> Unit) ?= null

    fun setOnItemClickListener(listener : (MovieModel.MovieModelResult) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        val diffUtil = object: DiffUtil.ItemCallback<MovieModel.MovieModelResult>() {
            override fun areContentsTheSame(oldItem: MovieModel.MovieModelResult, newItem: MovieModel.MovieModelResult) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: MovieModel.MovieModelResult, newItem: MovieModel.MovieModelResult) =
                oldItem.id == newItem.id
        }
    }
}