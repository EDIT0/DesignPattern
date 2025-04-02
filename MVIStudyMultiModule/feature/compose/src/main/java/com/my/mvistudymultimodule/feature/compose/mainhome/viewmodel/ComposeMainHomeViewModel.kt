package com.my.mvistudymultimodule.feature.compose.mainhome.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.my.mvistudymultimodule.core.base.BaseAndroidViewModel
import com.my.mvistudymultimodule.core.util.LogUtil
import com.my.mvistudymultimodule.domain.usecase.GetPopularMovieUseCase
import com.my.mvistudymultimodule.feature.compose.mainhome.event.ComposeMainHomeViewModelEvent
import com.my.mvistudymultimodule.feature.compose.mainhome.event.MovieListPagingUiEvent
import com.my.mvistudymultimodule.feature.compose.mainhome.state.MovieListPagingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComposeMainHomeViewModel @Inject constructor(
    app: Application,
    private val getPopularMovieUseCase: GetPopularMovieUseCase
): BaseAndroidViewModel(app) {

    private val scope = viewModelScope
    private var scopeJob: Job? = null

    private val language = "ko-KR"

    private val _movieListPagingUiEvent: MutableStateFlow<MovieListPagingUiEvent> = MutableStateFlow(MovieListPagingUiEvent.Idle)
    val movieListPagingUiState: StateFlow<MovieListPagingUiState> = _movieListPagingUiEvent.runningFold(MovieListPagingUiState()) { state, event ->
        when(event) {
            is MovieListPagingUiEvent.Idle -> {
                state.copy(movieList = MutableStateFlow(value = PagingData.empty()))
            }
            is MovieListPagingUiEvent.UpdateMovieList -> {
                state.copy(movieList = MutableStateFlow(value = event.movieList!!))
            }
        }
    }.stateIn(scope, SharingStarted.Eagerly, MovieListPagingUiState())

    fun handleViewModelEvent(composeMainHomeViewModelEvent: ComposeMainHomeViewModelEvent) {
        when(composeMainHomeViewModelEvent) {
            is ComposeMainHomeViewModelEvent.GetPopularMovie -> {
//                getPopularMovie()
                getPopularMoviePaging()
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