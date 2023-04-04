package com.example.mvvmexample1.presentation.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvmexample1.BuildConfig
import com.example.mvvmexample1.data.model.MovieModel
import com.example.mvvmexample1.data.util.NetworkManager
import com.example.mvvmexample1.domain.usecase.GetSearchMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel(
    private val app: Application,
    private val networkManager: NetworkManager,
    private val searchMoviesUseCase: GetSearchMoviesUseCase
) : AndroidViewModel(app){
    private val TAG = MainViewModel::class.java.simpleName

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private var currentSearchQuery = ""
    private val _movieList = MutableLiveData<ArrayList<MovieModel.MovieModelResult>>()
    val movieList: LiveData<ArrayList<MovieModel.MovieModelResult>> = _movieList
    private var isLoading = false
    private var totalPages = 0
    private var page = 0

    fun searchMovies() {
        if(!networkManager.checkNetworkState()) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 키워드에 대한 영화 검색 요청
                val result = searchMoviesUseCase.execute(
                    BuildConfig.API_KEY,
                    currentSearchQuery,
                    "en_US",
                    page
                )

                result
                    .onStart {
                        Log.i(TAG, "onStart()")
                        isLoading = true
                    }
                    .onCompletion {
                        Log.i(TAG, "onCompletion()")
                        isLoading = false
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

    }

}