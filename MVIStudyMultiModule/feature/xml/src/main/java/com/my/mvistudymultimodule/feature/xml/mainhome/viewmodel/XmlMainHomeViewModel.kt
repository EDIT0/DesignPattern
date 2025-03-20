package com.my.mvistudymultimodule.feature.xml.mainhome.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.my.mvistudymultimodule.core.base.BaseAndroidViewModel
import com.my.mvistudymultimodule.core.base.RequestResult
import com.my.mvistudymultimodule.core.util.LogUtil
import com.my.mvistudymultimodule.domain.usecase.GetPopularMovieUseCase
import com.my.mvistudymultimodule.feature.xml.mainhome.event.MovieListErrorUiEvent
import com.my.mvistudymultimodule.feature.xml.mainhome.event.MovieListPagingUiEvent
import com.my.mvistudymultimodule.feature.xml.mainhome.event.MovieListUiEvent
import com.my.mvistudymultimodule.feature.xml.mainhome.event.XmlMainHomeViewModelEvent
import com.my.mvistudymultimodule.feature.xml.mainhome.state.MovieListPagingUiState
import com.my.mvistudymultimodule.feature.xml.mainhome.state.MovieListUiState
import com.my.mvistudymultimodule.feature.xml.mainhome.view.adapter.MovieListPagingAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class XmlMainHomeViewModel @Inject constructor(
    app: Application,
    private val getPopularMovieUseCase: GetPopularMovieUseCase
): BaseAndroidViewModel(app) {

    private val scope = viewModelScope
    private var scopeJob: Job? = null

    val movieListPagingAdapter = MovieListPagingAdapter()

    private val language = "ko-KR"

    private val _movieListUiEvent: MutableStateFlow<MovieListUiEvent> = MutableStateFlow(MovieListUiEvent.Idle)
    val movieListUiState: StateFlow<MovieListUiState> = _movieListUiEvent.runningFold(MovieListUiState()) { state, event ->
        when(event) {
            is MovieListUiEvent.Idle -> {
                state
            }
            is MovieListUiEvent.UpdateMovieList -> {
                state.copy(movieList = event.movieList)
            }
            is MovieListUiEvent.UpdateLoading -> {
                state.copy(isLoading = event.isLoading)
            }
        }
    }.stateIn(scope, SharingStarted.Eagerly, MovieListUiState())

    private val _movieListPagingUiEvent: MutableStateFlow<MovieListPagingUiEvent> = MutableStateFlow(MovieListPagingUiEvent.Idle)
    val movieListPagingUiState: StateFlow<MovieListPagingUiState> = _movieListPagingUiEvent.runningFold(MovieListPagingUiState()) { state, event ->
        when(event) {
            is MovieListPagingUiEvent.Idle -> {
                state
            }
            is MovieListPagingUiEvent.UpdateMovieList -> {
                state.copy(movieList = event.movieList)
            }
            is MovieListPagingUiEvent.UpdateLoading -> {
                state.copy(isLoading = event.isLoading)
            }
        }
    }.stateIn(scope, SharingStarted.Eagerly, MovieListPagingUiState())

    private val _movieListErrorUiEvent = Channel<MovieListErrorUiEvent>()
    val movieListErrorUiEvent: Flow<MovieListErrorUiEvent> = _movieListErrorUiEvent.receiveAsFlow()

    fun handleViewModelEvent(xmlMainHomeViewModelEvent: XmlMainHomeViewModelEvent) {
        when(xmlMainHomeViewModelEvent) {
            is XmlMainHomeViewModelEvent.GetPopularMovie -> {
//                getPopularMovie()
                getPopularMoviePaging()
            }
        }
    }

    /**
     * 영화 데이터 요청 (일반)
     */
    private fun getPopularMovie() {
        scopeJob?.cancel()
        scopeJob = scope.launch {
            getPopularMovieUseCase.invoke(language, 1)
                .onStart {
                    _movieListUiEvent.value = MovieListUiEvent.UpdateLoading(isLoading = true)
                }
                .onCompletion {
                    _movieListUiEvent.value = MovieListUiEvent.UpdateLoading(isLoading = false)
                }
                .filter {
                    val errorCode = "${it.code}"
                    val errorMessage = "${it.message}"

                    if(it is RequestResult.Error) {
                        _movieListErrorUiEvent.send(MovieListErrorUiEvent.Fail(errorCode, errorMessage))
                        when(errorCode) {
                            "ERROR" -> {
                            }
                        }
                    }

                    if(it is RequestResult.DataEmpty) {
                        _movieListErrorUiEvent.send(MovieListErrorUiEvent.DataEmpty(true))
                    }

                    return@filter it is RequestResult.Success
                }
                .catch { e ->
                    _movieListErrorUiEvent.send(MovieListErrorUiEvent.ExceptionHandle(e))
                }
                .map {
                    it.resultData
                }
                .collect {
                    LogUtil.d_dev("영화 정보: ${it}")
                    _movieListUiEvent.value = MovieListUiEvent.UpdateMovieList(movieList = it?.movieModelResults)
                }
        }
    }

    /**
     * 영화 데이터 요청 (페이징)
     */
    private fun getPopularMoviePaging() {
        scopeJob?.cancel()
        scopeJob = scope.launch {
            getPopularMovieUseCase.invokePaging(language)
                .map { it ->
                    it.map { model ->
                        LogUtil.d_dev("페이징 데이터:  ${model}")
                        model
                    }
                }
                .cachedIn(scope)
                .collect {
                    _movieListPagingUiEvent.value = MovieListPagingUiEvent.UpdateMovieList(movieList = it)
                }
        }
    }

}