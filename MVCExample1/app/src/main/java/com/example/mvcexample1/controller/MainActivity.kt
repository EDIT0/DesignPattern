package com.example.mvcexample1.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.mvcexample1.controller.adapter.MovieAdapter
import com.example.mvcexample1.databinding.ActivityMainBinding
import com.example.mvcexample1.db.MovieDao
import com.example.mvcexample1.model.Movie
import com.example.mvcexample1.network.ApiService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.String
import javax.inject.Inject
import kotlin.Int
import kotlin.apply
import kotlin.properties.Delegates
import kotlin.reflect.KProperty


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    interface movieCallback {
        suspend fun isLoading(value: Boolean)
    }

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var binding: ActivityMainBinding
    @Inject
    lateinit var apiService: ApiService
    @Inject
    lateinit var movieDao: MovieDao
    lateinit var movie: Movie
    private lateinit var movieAdapter: MovieAdapter

    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setMovieModel()
        setMovieRV()
        buttonClickListener()
    }

    private fun setMovieModel() {
        movie = Movie(apiService, movieDao, object : MainActivity.movieCallback {
            override suspend fun isLoading(value: Boolean) {
                withContext(Dispatchers.Main) {
                    binding.swipeRefresh.isRefreshing = value
                }
            }
        })
    }

    private fun setMovieRV() {
        movieAdapter = MovieAdapter()

        binding.rvMovie.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
            addOnScrollListener(onScrollListener)
        }
    }

    private fun buttonClickListener() {
        binding.btnSearch.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                movie.setSearchQuery(binding.etInput.text.toString().trim())
                movie.setPage(1)
                movieAdapter.submitList(emptyList())

                val result = movie.searchMovies()
                withContext(Dispatchers.Main) {
                    movieAdapter.apply {
                        submitList(result) {
                            notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        movieAdapter.setOnItemClickListener { i, movieModelResult ->
            lifecycleScope.launch(Dispatchers.IO) {
                kotlin.runCatching {
                    movie.saveMovie(movieModelResult)
                }.onFailure {
                    withContext(Dispatchers.Main) {
                        toast?.cancel()
                        toast = Toast.makeText(binding.root.context, "Already Saved", Toast.LENGTH_SHORT)
                        toast?.show()
                    }
                }.onSuccess {
                    withContext(Dispatchers.Main) {
                        toast?.cancel()
                        toast = Toast.makeText(binding.root.context, "Save ${movieModelResult.title}", Toast.LENGTH_SHORT)
                        toast?.show()
                    }
                }
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            lifecycleScope.launch(Dispatchers.IO) {
                movie.setSearchQuery(binding.etInput.text.toString().trim())
                movie.setPage(1)
                movieAdapter.submitList(emptyList())

                val result = movie.searchMovies()
                withContext(Dispatchers.Main) {
                    movieAdapter.apply {
                        submitList(result) {
                            notifyDataSetChanged()
                            binding.swipeRefresh.isRefreshing = false
                        }
                    }
                }
            }
        }

        binding.btnRefresh.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                movie.setSearchQuery(binding.etInput.text.toString().trim())
                movie.setPage(1)
                movieAdapter.submitList(emptyList())

                val result = movie.searchMovies()
                withContext(Dispatchers.Main) {
                    movieAdapter.apply {
                        submitList(result) {
                            notifyDataSetChanged()
                            binding.swipeRefresh.isRefreshing = false
                        }
                    }
                }
            }
        }

        binding.btnSavedMovies.setOnClickListener {
            startActivity(Intent(this, SavedMoviesActivity::class.java))
        }
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            val lastVisibleItemPosition =
                (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() // 화면에 보이는 마지막 아이템의 position
            val itemTotalCount = recyclerView.adapter!!.itemCount - 1 // 어댑터에 등록된 아이템의 총 개수 -1

            // 스크롤이 끝에 도달했는지 확인
            movie.apply {
                if (lastVisibleItemPosition == itemTotalCount) {
                    if (getLoadingState()) {

                    } else {
                        if (getTotalPages() != getCurrentPage()) {
                            lifecycleScope.launch(Dispatchers.IO) {
                                Log.i(TAG, "TotalPages = ${getTotalPages()}")
                                val result = searchMovies()
                                withContext(Dispatchers.Main) {
                                    movieAdapter.apply {
                                        submitList(result) {
                                            notifyDataSetChanged()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }
}