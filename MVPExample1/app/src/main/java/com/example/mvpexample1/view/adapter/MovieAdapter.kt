package com.example.mvpexample1.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvpexample1.BuildConfig
import com.example.mvpexample1.R
import com.example.mvpexample1.databinding.MovieListItemBinding
import com.example.mvpexample1.model.data.MovieModel

class MovieAdapter : ListAdapter<MovieModel.MovieModelResult, MovieAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(val binding: MovieListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: MovieModel.MovieModelResult) {

            binding.apply {
                tvTitle.text = data.title
                Glide.with(binding.root.context)
                    .load(BuildConfig.BASE_MOVIE_POSTER + data.posterPath)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(binding.ivThumbnail)
                tvReleaseDate.text = data.releaseDate
            }


            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(adapterPosition, data)
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