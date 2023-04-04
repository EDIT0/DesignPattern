package com.example.mvvmarchitecturestudy2.presentation.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmarchitecturestudy2.BuildConfig
import com.example.mvvmarchitecturestudy2.R
import com.example.mvvmarchitecturestudy2.data.model.MovieModel
import com.example.mvvmarchitecturestudy2.data.model.MovieReviewModel
import com.example.mvvmarchitecturestudy2.presentation.viewmodel.MainViewModel

@BindingAdapter("setMovieAdapterItems")
fun RecyclerView.setMovieAdapterItems(items: MutableList<MovieModel.MovieModelResult>?) {
    items?.let{
        (adapter as MovieAdapter).submitList(it.toMutableList())
    }
}

@BindingAdapter("setMovieReviewAdapterItems")
fun RecyclerView.setMovieReviewAdapterItems(items: MutableList<MovieReviewModel.Result>?) {
    items?.let{
        (adapter as MovieReviewAdapter).submitList(it.toMutableList())
    }
}

@BindingAdapter("setUrlImage")
fun ImageView.setUrlImage(url: String?) {
    Glide.with(this.context)
        .load(BuildConfig.BASE_MOVIE_POSTER + url)
        .placeholder(R.drawable.ic_launcher_background)
        .error(R.drawable.ic_launcher_foreground)
        .into(this)
}

@BindingAdapter("setImage")
fun ImageView.setImage(url: String?) {
    Glide.with(this.context)
        .load(url)
        .placeholder(R.drawable.ic_launcher_background)
        .error(R.drawable.ic_launcher_foreground)
        .into(this)
}