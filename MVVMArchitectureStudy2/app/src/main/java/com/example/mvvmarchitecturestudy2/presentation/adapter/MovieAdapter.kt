package com.example.mvvmarchitecturestudy2.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmarchitecturestudy2.BuildConfig
import com.example.mvvmarchitecturestudy2.data.model.MovieModel
import com.example.mvvmarchitecturestudy2.databinding.MovieListItemBinding

class MovieAdapter : ListAdapter<MovieModel.MovieModelResult, MovieAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(val binding: MovieListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(popularMovieModelResult: MovieModel.MovieModelResult) {

            binding.movieListItem = popularMovieModelResult
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(adapterPosition, popularMovieModelResult)
                }
            }
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
                oldItem.id == newItem.id
        }
    }

}