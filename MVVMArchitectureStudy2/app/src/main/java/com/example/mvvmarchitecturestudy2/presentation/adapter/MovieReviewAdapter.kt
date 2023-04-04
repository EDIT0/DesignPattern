package com.example.mvvmarchitecturestudy2.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmarchitecturestudy2.data.model.MovieModel
import com.example.mvvmarchitecturestudy2.data.model.MovieReviewModel
import com.example.mvvmarchitecturestudy2.databinding.MovieListItemBinding
import com.example.mvvmarchitecturestudy2.databinding.MovieReviewItemBinding

class MovieReviewAdapter : ListAdapter<MovieReviewModel.Result, MovieReviewAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MovieReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(val binding: MovieReviewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: MovieReviewModel.Result) {

            binding.movieReviewItem = data
            binding.executePendingBindings()

//            binding.root.setOnClickListener {
//                onItemClickListener?.let {
//                    it(adapterPosition, data)
//                }
//            }
        }
    }

//    private var onItemClickListener : ((Int, MovieReviewModel.Result) -> Unit)? = null
//
//    fun setOnItemClickListener(listener : (Int, MovieReviewModel.Result) -> Unit) {
//        onItemClickListener = listener
//    }

    companion object {
        val diffUtil = object: DiffUtil.ItemCallback<MovieReviewModel.Result>() {
            override fun areContentsTheSame(oldItem: MovieReviewModel.Result, newItem: MovieReviewModel.Result) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: MovieReviewModel.Result, newItem: MovieReviewModel.Result) =
                oldItem.id == newItem.id
        }
    }

}