package com.example.mvvmarchitecturestudy2.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmarchitecturestudy2.R
import com.example.mvvmarchitecturestudy2.databinding.ActivityMainBinding
import com.example.mvvmarchitecturestudy2.presentation.adapter.MovieAdapter
import com.example.mvvmarchitecturestudy2.presentation.base.BaseActivity
import com.example.mvvmarchitecturestudy2.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val TAG = MainActivity::class.simpleName

    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = mainViewModel

        initRecyclerView()
        viewModelToast()
        editTextListener()

        mainViewModel.getPopularMovies()

    }

    private fun initRecyclerView() {
        binding.rvMovies.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(applicationContext)
            addOnScrollListener(onScrollListener)
        }

        movieAdapter.setOnItemClickListener { position, movieModelResult ->
            val intent = Intent(mActivity, MovieInfoActivity::class.java)
            intent.putExtra("movieId", mainViewModel.movieList.value?.get(position)?.id)
            startActivity(intent)
        }
    }

    private fun viewModelToast() {
        with(mainViewModel) {
            toastMsg.observe(mActivity as LifecycleOwner, {
                when (toastMsg.value) {
                    MainViewModel.MessageSet.SUCCESS -> showToast(getString(R.string.toast_success))
                    MainViewModel.MessageSet.NO_DATA -> showToast(getString(R.string.toast_no_data))
                    MainViewModel.MessageSet.ERROR -> showToast(getString(R.string.toast_error))
                    MainViewModel.MessageSet.NETWORK_NOT_CONNECTED -> showToast(getString(R.string.toast_network_disconnect))
                    MainViewModel.MessageSet.NO_SEARCH -> showToast(getString(R.string.toast_no_search))
                    MainViewModel.MessageSet.SEARCH_SUCCESS -> {
                        Log.i(TAG, "검색 ${binding.etSearch.text}")
                        hideKeyboard()
                    }
                }
            })
        }
    }

    private fun editTextListener() {
        binding.etSearch.setOnKeyListener(View.OnKeyListener { p0, p1, p2 ->
            if (p2.action == KeyEvent.ACTION_DOWN && p1 == KeyEvent.KEYCODE_ENTER) {
                mainViewModel.getSearchMovies("search")
                return@OnKeyListener true
            }
            false
        })
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            val lastVisibleItemPosition =
                (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() // 화면에 보이는 마지막 아이템의 position
            val itemTotalCount = recyclerView.adapter!!.itemCount - 1 // 어댑터에 등록된 아이템의 총 개수 -1

            // 스크롤이 끝에 도달했는지 확인
            if (lastVisibleItemPosition == itemTotalCount) {
                if (mainViewModel.isLoading.value == true) {

                } else {
                    if (mainViewModel.getTotalPages() != mainViewModel.getPage() && mainViewModel.movieList.value?.size!! > 0) {
//                        mainViewModel.page++
                        if (mainViewModel.getCurrentSearch().isNotEmpty()) {
                            mainViewModel.getSearchMovies("reload")
                        } else {
                            mainViewModel.getPopularMovies()
                        }

                    }
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }
}