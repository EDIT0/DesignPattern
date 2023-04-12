package com.example.mvvmarchitecturestudy.presentation.ui.activity

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mvvmarchitecturestudy.BuildConfig
import com.example.mvvmarchitecturestudy.R
import com.example.mvvmarchitecturestudy.data.model.MovieModelResult
import com.example.mvvmarchitecturestudy.databinding.ActivityMainBinding
import com.example.mvvmarchitecturestudy.databinding.ActivityMovieInfoBinding
import com.example.mvvmarchitecturestudy.presentation.ui.fragment.SearchMovieFragment
import com.example.mvvmarchitecturestudy.presentation.viewmodel.MainViewModel
import com.example.mvvmarchitecturestudy.presentation.viewmodel.MovieInfoViewModel
import com.example.mvvmarchitecturestudy.presentation.viewmodel.MovieInfoViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieInfoActivity : AppCompatActivity() {

    private val CLASS_NAME = MovieInfoActivity::class.java.simpleName

    private lateinit var binding : ActivityMovieInfoBinding

    lateinit var movieInfoViewModel: MovieInfoViewModel
    @Inject
    lateinit var movieInfoViewModelFactory: MovieInfoViewModelFactory
    lateinit var movieInfo : MovieModelResult
    private var thumbnailHeight = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieInfoViewModel = ViewModelProvider(this, movieInfoViewModelFactory).get(MovieInfoViewModel::class.java)

        getIntentInfo()
        initData()
        buttonOnClickListener()


//        ViewCompat.setTransitionName(binding.ivThumbnail,"thumbnailTransition")
//        ViewCompat.setTransitionName(binding.tvMovieInfo,"titleTransition")
        getThumbnailLayout()
        scrollListener()
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
        binding.tvMovieInfo.text = str + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n" + "\nHello\n"
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

    private fun scrollListener() {
        binding.scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val alpha = 1 - scrollY / thumbnailHeight.toFloat() // 스크롤 위치에 따라 투명도 계산
            val scale = 1 + scrollY / thumbnailHeight.toFloat() // 스크롤 위치에 따라 스케일 계산
            Log.i(MainActivity.TAG + CLASS_NAME, "${scrollY} / ${thumbnailHeight} / ${alpha} / ${scale}")
            if (alpha >= 0) {
                binding.ivThumbnail
                    .animate()
                    .alpha(alpha)
//                    .scaleX(scale)
//                    .scaleY(scale)
                    .setDuration(0)
                    .start()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val scrollBounds = Rect()
        binding.scrollView.getHitRect(scrollBounds)
        if (binding.ivThumbnail.getGlobalVisibleRect(scrollBounds)) {
            finishAfterTransition()
        } else {
            /**
             * 연결된 뷰가 가려진 경우, 다른 애니메이션을 적용해서 뒤로가기를 처리해준다.
             * */
            window.sharedElementsUseOverlay = false
            window.sharedElementExitTransition = null
            window.sharedElementEnterTransition = null
            finish()
            overridePendingTransition(0, 0)
        }
    }

    private fun getThumbnailLayout() {
        binding.ivThumbnail.getViewTreeObserver().addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.ivThumbnail.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                Log.i(MainActivity.TAG + CLASS_NAME, "넓이: ${binding.ivThumbnail.width} / 높이: ${binding.ivThumbnail.getHeight()}")
                thumbnailHeight = binding.ivThumbnail.height
//                binding.scrollView.setPadding(0, 0, 0, binding.ivThumbnail.getHeight())
//                binding.scrollView.setFadingEdgeLength(binding.fabLayout.getHeight())
            }
        })
    }
}