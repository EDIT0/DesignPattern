package com.example.mvpexample1.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvpexample1.databinding.ActivityMainBinding
import com.example.mvpexample1.model.db.MovieDao
import com.example.mvpexample1.model.network.ApiService
import com.example.mvpexample1.model.usecase.GetSearchMoviesUseCase
import com.example.mvpexample1.model.usecase.InsertMovieUseCase
import com.example.mvpexample1.model.util.ERROR
import com.example.mvpexample1.model.util.NO_DATA
import com.example.mvpexample1.presenter.MainContract
import com.example.mvpexample1.presenter.MainPresenter
import com.example.mvpexample1.view.adapter.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainContract.View {

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainPresenter: MainPresenter

    @Inject
    lateinit var getSearchMoviesUseCase: GetSearchMoviesUseCase
    @Inject
    lateinit var insertMovieUseCase: InsertMovieUseCase

    private lateinit var movieAdapter: MovieAdapter

    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setPresenter()
        setMovieRV()
        buttonClickListener()
        observer()

    }

    override suspend fun showProgress(value: Boolean) {
        withContext(Dispatchers.Main) {
            binding.swipeRefresh.isRefreshing = value
        }
    }

    override fun showToast(str: String) {
        toast?.cancel()
        toast = Toast.makeText(binding.root.context, str, Toast.LENGTH_SHORT)
        toast?.show()
    }

    private fun setPresenter() {
        mainPresenter = MainPresenter(
            this,
            getSearchMoviesUseCase,
            insertMovieUseCase
        )
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
                mainPresenter.setSearchQuery(binding.etInput.text.toString().trim())
                mainPresenter.setPage(1)
                movieAdapter.submitList(emptyList())

                mainPresenter.searchMovies()
            }
        }

        movieAdapter.setOnItemClickListener { i, movieModelResult ->
            lifecycleScope.launch(Dispatchers.IO) {
                kotlin.runCatching {
                    mainPresenter.saveMovie(movieModelResult)
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
                mainPresenter.setSearchQuery(binding.etInput.text.toString().trim())
                mainPresenter.setPage(1)
                movieAdapter.submitList(emptyList())

                mainPresenter.searchMovies()
            }
        }

        binding.btnRefresh.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                mainPresenter.setSearchQuery(binding.etInput.text.toString().trim())
                mainPresenter.setPage(1)
                movieAdapter.submitList(emptyList())

                mainPresenter.searchMovies()
            }
        }

        binding.btnSavedMovies.setOnClickListener {
            startActivity(Intent(this, SavedMoviesActivity::class.java))
        }
    }

    private fun observer() {
        mainPresenter.movieList.observe(this) {
            Log.i("MYTAG", "라이브데이터 관찰 : ${it}")
            movieAdapter.apply {
                submitList(it) {
                    notifyDataSetChanged()
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }

        mainPresenter.error.observe(this) {
            when(it) {
                NO_DATA -> {
                    toast?.cancel()
                    toast = Toast.makeText(binding.root.context, "No Data", Toast.LENGTH_SHORT)
                    toast?.show()
                }
                ERROR -> {
                    toast?.cancel()
                    toast = Toast.makeText(binding.root.context, "Error", Toast.LENGTH_SHORT)
                    toast?.show()
                }
            }
        }
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            val lastVisibleItemPosition =
                (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() // 화면에 보이는 마지막 아이템의 position
            val itemTotalCount = recyclerView.adapter!!.itemCount - 1 // 어댑터에 등록된 아이템의 총 개수 -1

            // 스크롤이 끝에 도달했는지 확인
            mainPresenter.apply {
                if (lastVisibleItemPosition == itemTotalCount) {
                    if (getLoadingState()) {

                    } else {
                        if (getTotalPages() != getCurrentPage()) {
                            lifecycleScope.launch(Dispatchers.IO) {
                                Log.i(TAG, "TotalPages = ${getTotalPages()}")
                                searchMovies()
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

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.releaseView()
    }
}