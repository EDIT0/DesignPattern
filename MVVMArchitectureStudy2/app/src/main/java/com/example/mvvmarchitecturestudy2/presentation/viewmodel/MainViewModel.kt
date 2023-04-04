package com.example.mvvmarchitecturestudy2.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarchitecturestudy2.data.model.MovieModel
import com.example.mvvmarchitecturestudy2.data.util.ERROR
import com.example.mvvmarchitecturestudy2.data.util.NO_DATA
import com.example.mvvmarchitecturestudy2.data.util.NetworkManager
import com.example.mvvmarchitecturestudy2.domain.usecase.GetPopularMoviesUseCase
import com.example.mvvmarchitecturestudy2.domain.usecase.GetSearchMoviesUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel(
    private val networkManager: NetworkManager,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getSearchMoviesUseCase: GetSearchMoviesUseCase
) : ViewModel() {

    private val TAG = MainViewModel::class.simpleName

    private val language = "en_US"
    private var page = 1
    private var totalPages = 0

    var search = ""
    private var currentSearch = ""

    private val _movieList = MutableLiveData<MutableList<MovieModel.MovieModelResult>?>()
    val movieList: MutableLiveData<MutableList<MovieModel.MovieModelResult>?> = _movieList

    private val _toastMsg = MutableLiveData<MessageSet>()
    val toastMsg: LiveData<MessageSet> get() = _toastMsg

    fun getPage() : Int = page

    fun initPage() {
        page = 1
    }

    fun getTotalPages() : Int {
        return totalPages
    }

    fun getCurrentSearch() : String {
        return currentSearch
    }

    fun getPopularMovies() {
        if(!networkManager.checkNetworkState()) {
            return
        }

        viewModelScope.launch {
            Log.i(TAG, "현재 스레드: ${Thread.currentThread().name}")
            getPopularMoviesUseCase.execute(language, page)
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
                        Log.i(TAG, "인기 영화 ERROR: ${e.message}")
                        _toastMsg.value = MessageSet.ERROR
                    } else {
                        Log.i(TAG, "인기 영화 오류 : ${e.message}")
                        _toastMsg.value = MessageSet.ERROR
                    }
                }
                .collect {
                    Log.i(TAG, "현재 스레드 collect: ${Thread.currentThread().name}\n${it}")
                    totalPages = it.totalPages
                    if (page == 1) {
                        _movieList.value = it.movieModelResults as ArrayList
                        _toastMsg.value = MessageSet.SUCCESS
                        page++
                    } else {
                        val pagingMovieList = _movieList.value
                        pagingMovieList?.addAll(it.movieModelResults)
                        _movieList.postValue(pagingMovieList!!)
                        _toastMsg.value = MessageSet.SUCCESS
                        page++
                    }
                }
        }

    }


    fun getSearchMovies(state: String) {
        if(!networkManager.checkNetworkState()) {
            return
        }

        viewModelScope.launch {
            if(state == "search") {
                if (search != "") {
                    if(search != currentSearch) {
                        if (search != currentSearch) {
                            _movieList.value = null
                            currentSearch = search
                            page = 1
                            _toastMsg.value = MessageSet.SEARCH_SUCCESS
                        }
                    } else {
                        Log.i(TAG, "검색어 같음")
                        return@launch
                    }
                } else {
                    _toastMsg.value = MessageSet.NO_SEARCH
                    return@launch
                }
            }

            getSearchMoviesUseCase.execute(currentSearch, language, page)
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
                        Log.i(TAG, "영화 검색 ERROR: ${e.message}")
                        _toastMsg.value = MessageSet.ERROR
                    } else {
                        Log.i(TAG, "영화 검색 오류 : ${e.message}")
                        _toastMsg.value = MessageSet.ERROR
                    }
                }
                .collect {
                    totalPages = it.totalPages
                    if (page == 1) {
                        _movieList.value = it.movieModelResults as ArrayList
                        _toastMsg.value = MessageSet.SUCCESS
                        page++
                    } else {
                        val pagingMovieList = _movieList.value
                        pagingMovieList?.addAll(it.movieModelResults)
                        _movieList.postValue(pagingMovieList!!)
                        _toastMsg.value = MessageSet.SUCCESS
                        page++
                    }
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
}