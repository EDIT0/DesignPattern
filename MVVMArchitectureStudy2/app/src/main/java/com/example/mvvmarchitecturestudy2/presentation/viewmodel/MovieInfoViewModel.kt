package com.example.mvvmarchitecturestudy2.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarchitecturestudy2.data.model.MovieDetailModel
import com.example.mvvmarchitecturestudy2.data.model.MovieModel
import com.example.mvvmarchitecturestudy2.data.model.MovieReviewModel
import com.example.mvvmarchitecturestudy2.data.util.ERROR
import com.example.mvvmarchitecturestudy2.data.util.NO_DATA
import com.example.mvvmarchitecturestudy2.data.util.NetworkManager
import com.example.mvvmarchitecturestudy2.domain.usecase.GetMovieDetailUseCase
import com.example.mvvmarchitecturestudy2.domain.usecase.GetMovieReviewUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MovieInfoViewModel(
    private val networkManager: NetworkManager,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getMovieReviewUseCase: GetMovieReviewUseCase
) : ViewModel() {

    private val TAG = MovieInfoViewModel::class.simpleName

    private var movieId = 0

    fun setMovieId(id: Int) {
        movieId = id
    }

    fun getMovieId() : Int {
        return movieId
    }

    private val _toastMsg = MutableLiveData<MessageSet>()
    val toastMsg: LiveData<MessageSet> get() = _toastMsg

    private val language = "en-US"

    private val _movieDetailResponse = MutableLiveData<MovieDetailModel>()
    val movieDetailResponse: MutableLiveData<MovieDetailModel> = _movieDetailResponse

    fun getMovieDetail() {
        if(!networkManager.checkNetworkState()) {
            return
        }

        viewModelScope.launch {
            getMovieDetailUseCase.execute(movieId, language)
                .onStart {
                    showProgress()
                }
                .onCompletion {
                    hideProgress()
                }
                .catch { e ->
                    if (e.message.equals(NO_DATA)) {
                        Log.i(TAG, "데이터 없음")
                        _toastMsg.value = MessageSet.NO_DATA
                    } else if(e.message.equals(ERROR)) {
                        Log.i(TAG, "영화 정보 ERROR: ${e.message}")
                        _toastMsg.value = MessageSet.ERROR
                    } else {
                        Log.i(TAG, "영화 정보 오류 : ${e.message}")
                        _toastMsg.value = MessageSet.ERROR
                    }
                }
                .collect {
                    Log.i(TAG, "현재 스레드 collect: ${Thread.currentThread().name}\n${it}")
                    _movieDetailResponse.value = it

                    _toastMsg.value = MessageSet.SUCCESS
                }
        }

    }

    private var movieReviewPage = 1
    private var movieReviewTotalPages = 0

    fun getReviewPage() : Int = movieReviewPage

    fun getReviewTotalPages() = movieReviewTotalPages


    private val _movieReviewList = MutableLiveData<MutableList<MovieReviewModel.Result>>()
    val movieReviewList: LiveData<MutableList<MovieReviewModel.Result>> = _movieReviewList

    fun getMovieReview() {
        if(!networkManager.checkNetworkState()) {
            return
        }

        viewModelScope.launch {
            getMovieReviewUseCase.execute(movieId, language, movieReviewPage)
                .onStart {
                    showProgress()
                }
                .onCompletion {
                    hideProgress()
                }
                .catch { e ->
                    if (e.message.equals(NO_DATA)) {
                        Log.i(TAG, "데이터 없음")
                        _toastMsg.value = MessageSet.NO_DATA
                    } else if(e.message.equals(ERROR)) {
                        Log.i(TAG, "영화 정보 ERROR: ${e.message}")
                        _toastMsg.value = MessageSet.ERROR
                    } else {
                        Log.i(TAG, "영화 정보 오류 : ${e.message}")
                        _toastMsg.value = MessageSet.ERROR
                    }
                }
                .collect {
                    Log.i(TAG, "현재 스레드 collect: ${Thread.currentThread().name}\n${it}")
                    movieReviewTotalPages = it.totalPages
                    movieReviewPage = it.page

                    if (movieReviewPage == 1) {
                        _movieReviewList.value = it.results as ArrayList
                    } else {
                        val pagingMovieList = _movieReviewList.value
                        pagingMovieList?.addAll(it.results)
                        _movieReviewList.postValue(pagingMovieList!!)
                    }
                    movieReviewPage++

                    _toastMsg.value = MessageSet.SUCCESS
                }
        }

    }

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun showProgress() {
        _isLoading.value = true
    }

    fun hideProgress() {
        _isLoading.value = false
    }

    enum class MessageSet {
        NO_DATA,
        NETWORK_NOT_CONNECTED,
        ERROR,
        SUCCESS,
        NO_SEARCH,
        SEARCH_SUCCESS
    }

    fun getPoster() {
        Log.i("Fragment", "getPoster()")
    }

    fun getInfo() {
        Log.i("Fragment", "getInfo()")
    }

    fun getReview() {
        Log.i("Fragment", "getReview()")
    }
}