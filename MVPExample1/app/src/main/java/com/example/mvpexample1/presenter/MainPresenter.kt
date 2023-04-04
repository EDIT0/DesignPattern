package com.example.mvpexample1.presenter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvpexample1.BuildConfig
import com.example.mvpexample1.model.data.MovieModel
import com.example.mvpexample1.model.db.MovieDao
import com.example.mvpexample1.model.network.ApiService
import com.example.mvpexample1.model.usecase.GetSearchMoviesUseCase
import com.example.mvpexample1.model.usecase.InsertMovieUseCase
import com.example.mvpexample1.model.util.ERROR
import com.example.mvpexample1.model.util.NO_DATA
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class MainPresenter(
    private val mainContractView: MainContract.View,
    private val getSearchMoviesUseCase: GetSearchMoviesUseCase,
    private val insertMovieUseCase: InsertMovieUseCase
) : MainContract.Presenter {

    private val TAG = MainPresenter::class.java.simpleName

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private var currentSearchQuery = ""
    private val _movieList = MutableLiveData<ArrayList<MovieModel.MovieModelResult>>()
    val movieList: LiveData<ArrayList<MovieModel.MovieModelResult>> = _movieList
    private var isLoading = false
    private var totalPages = 0
    private var page = 0

    override fun setSearchQuery(query: String) {
        currentSearchQuery = query
    }

    override fun getLoadingState(): Boolean = isLoading

    override fun getTotalPages(): Int = totalPages

    override fun getCurrentPage(): Int = page

    override fun setPage(page: Int) {
        this.page = page
    }

    override suspend fun searchMovies() {
        try {
            // 키워드에 대한 영화 검색 요청
            val result = getSearchMoviesUseCase.execute(
                BuildConfig.API_KEY,
                currentSearchQuery,
                "en_US",
                page
            )

            result
                .onStart {
                    Log.i(TAG, "onStart()")
                    isLoading = true
                    mainContractView.showProgress(isLoading)
                }
                .onCompletion {
                    Log.i(TAG, "onCompletion()")
                    isLoading = false
                    mainContractView.showProgress(isLoading)
                }
                .catch {
                    // 앞에서 예외가 발생했다면 이쪽으로 빠진다. 또 예외를 발생시킨다.
                    Log.i(TAG, "catch : ${it.message}")
                    _error.postValue(it.message)
                }
                .collect {
                    Log.i(TAG, "${it}")
                    // 페이지가 1이면 기존 리스트를 비우고 시작
                    totalPages = it.totalPages
                    if(page == 1) {
                        Log.i(TAG, "Clear Movie List")
                        _movieList.postValue(it.movieModelResults as ArrayList)
                    } else {
                        val tempList = movieList.value as ArrayList
                        tempList.addAll(it.movieModelResults)
                        _movieList.postValue(tempList)
                    }
                    page++
                }

        } catch (e: Exception) {
            Log.i(TAG, "Request Catch : ${e.message}")
        }
    }

    override suspend fun saveMovie(data: MovieModel.MovieModelResult) {
        insertMovieUseCase.execute(data)
    }

    override fun releaseView() {
        mainContractView.showToast("releaseView")
    }
}
