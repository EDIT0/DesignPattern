package com.example.mvvmarchitecturestudy2.presentation.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.mvvmarchitecturestudy2.BuildConfig
import com.example.mvvmarchitecturestudy2.R
import com.example.mvvmarchitecturestudy2.databinding.FragmentPosterBinding
import com.example.mvvmarchitecturestudy2.presentation.ui.activity.MovieInfoActivity
import com.example.mvvmarchitecturestudy2.presentation.viewmodel.MovieInfoViewModel

class PosterFragment : Fragment() {

    private val TAG = PosterFragment::class.simpleName

    private lateinit var fragmentPosterBinding: FragmentPosterBinding

    lateinit var movieInfoViewModel: MovieInfoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView() ${this.hashCode()}")
        fragmentPosterBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_poster, container, false)
        return fragmentPosterBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated()")

        movieInfoViewModel = (activity as MovieInfoActivity).movieInfoViewModel

        movieInfoViewModel.getMovieDetail()

        observeListener()
    }

    private fun observeListener() {
        movieInfoViewModel.movieDetailResponse.observe(viewLifecycleOwner, {
            Log.i(TAG, "value : ${it}")

            fragmentPosterBinding.movieDetail = it

//            Glide.with(fragmentPosterBinding.ivPoster.context)
//                .load(BuildConfig.BASE_MOVIE_POSTER + it.posterPath)
//                .error(R.drawable.ic_launcher_foreground)
//                .into(fragmentPosterBinding.ivPoster)
        })
    }

}