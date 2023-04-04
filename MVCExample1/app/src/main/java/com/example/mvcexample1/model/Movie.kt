package com.example.mvcexample1.model

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.mvcexample1.BuildConfig
import com.example.mvcexample1.controller.MainActivity
import com.example.mvcexample1.db.MovieDao
import com.example.mvcexample1.model.data.MovieModel
import com.example.mvcexample1.network.ApiService
import com.example.mvcexample1.util.ERROR
import com.example.mvcexample1.util.NO_DATA
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class Movie(
    private val apiService: ApiService,
    private val movieDao: MovieDao,
    private val movieCallback: MainActivity.movieCallback
) {

    private val TAG = Movie::class.java.simpleName

    private var currentSearchQuery = ""
    private var movieList = ArrayList<MovieModel.MovieModelResult>()
    private var isLoading = false
    private var totalPages = 0
    private var page = 0

    fun setSearchQuery(query: String) {
        currentSearchQuery = query
    }
    fun getLoadingState() : Boolean = isLoading
    fun getTotalPages() : Int = totalPages
    fun getCurrentPage() : Int = page
    fun setPage(page : Int) {
        this.page = page
    }

    suspend fun searchMovies() : List<MovieModel.MovieModelResult> {
        try {
            val result = flow<MovieModel> {
                // 키워드에 대한 영화 검색 요청
                val response = apiService.getSearchMovies(
                    BuildConfig.API_KEY,
                    currentSearchQuery,
                    "en_US",
                    page
                )

                // 요청에 대한 응답
                if(response.isSuccessful) {
                    // 데이터가 없으면 NO_DATA 이름으로 예외 발생
                    if(response.body()?.movieModelResults?.isEmpty() == true) {
                        throw Exception(NO_DATA)
                    } else {
                        // 데이터가 있다면 발행
                        response.body()?.let {
                            delay(1000L)
                            emit(it)
                        }
                    }
                } else {
                    throw Exception(ERROR)
                }
            }

            result
                .onStart {
                    Log.i(TAG, "onStart()")
                    isLoading = true
                    movieCallback.isLoading(isLoading)
                }
                .onCompletion {
                    Log.i(TAG, "onCompletion()")
                    isLoading = false
                    movieCallback.isLoading(isLoading)
                }
                .catch {
                    // 앞에서 예외가 발생했다면 이쪽으로 빠진다. 또 예외를 발생시킨다.
                    Log.i(TAG, "catch : ${it.message}")
                    throw Exception(it.message)
                }
                .collect {
                    Log.i(TAG, "${it}")
                    // 페이지가 1이면 기존 리스트를 비우고 시작
                    if(page == 1) {
                        Log.i(TAG, "Clear Movie List")
                        movieList.clear()
                    }
                    totalPages = it.totalPages
                    val tempArray = it.movieModelResults as ArrayList
                    movieList.addAll(tempArray)
                    page++
                }

            return movieList

        } catch (e: Exception) {
            // 예외 발생 마지막 부분 이쪽을 거친 후 빈 리스트 리턴
            Log.i(TAG, "Request Catch : ${e.message}")
        }

        return emptyList<MovieModel.MovieModelResult>()
    }


    val savedMovieList = MutableLiveData<ArrayList<MovieModel.MovieModelResult>>()
    suspend fun getSavedMovies() {
        try {
            val result = movieDao.getAllSavedMovies()

            result.collect {
                savedMovieList.postValue(it as ArrayList)
            }

        } catch (e : Exception) {

        }
    }

    suspend fun saveMovie(data: MovieModel.MovieModelResult) {
        movieDao.insertMovie(data)
    }

    suspend fun deleteMovie(data: MovieModel.MovieModelResult) {
        movieDao.deleteSavedMovies(data)
    }
}