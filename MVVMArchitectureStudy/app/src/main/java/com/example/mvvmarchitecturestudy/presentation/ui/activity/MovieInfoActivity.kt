package com.example.mvvmarchitecturestudy.presentation.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mvvmarchitecturestudy.BuildConfig
import com.example.mvvmarchitecturestudy.data.model.MovieModelResult
import com.example.mvvmarchitecturestudy.databinding.ActivityMainBinding
import com.example.mvvmarchitecturestudy.databinding.ActivityMovieInfoBinding
import com.example.mvvmarchitecturestudy.presentation.viewmodel.MainViewModel
import com.example.mvvmarchitecturestudy.presentation.viewmodel.MovieInfoViewModel
import com.example.mvvmarchitecturestudy.presentation.viewmodel.MovieInfoViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieInfoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMovieInfoBinding

    lateinit var movieInfoViewModel: MovieInfoViewModel
    @Inject
    lateinit var movieInfoViewModelFactory: MovieInfoViewModelFactory


    lateinit var movieInfo : MovieModelResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieInfoViewModel = ViewModelProvider(this, movieInfoViewModelFactory).get(MovieInfoViewModel::class.java)

        getIntentInfo()
        initData()
        buttonOnClickListener()



    }

    private fun getIntentInfo() {
        val intent = intent
        movieInfo = intent.getSerializableExtra("MovieInfo") as MovieModelResult // 직렬화된 객체를 받는 방법
    }

    private fun initData() {
        Glide.with(binding.ivThumbnail)
            .load(BuildConfig.BASE_MOVIE_POSTER + movieInfo.posterPath)
            .into(binding.ivThumbnail)

        val str = movieInfo.title + "\n\n" + movieInfo.releaseDate + "\n\n" + movieInfo.popularity + "\n\n" + movieInfo.overview
        binding.tvMovieInfo.text = str
    }

    private fun buttonOnClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.fabSaveMovie.setOnClickListener {
            movieInfoViewModel.saveMovie(movieInfo)
            Toast.makeText(this, "Save Movie", Toast.LENGTH_SHORT).show()
        }
    }
}