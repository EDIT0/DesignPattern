package com.my.mvistudymultimodule.feature.compose.mainhome.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
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

    private val _sideEffectEvent = Channel<SideEffectEvent>()
    val sideEffectEvent = _sideEffectEvent.receiveAsFlow()

    private val _movieListPagingUiState = MutableStateFlow(MovieListPagingUiState())
    val movieListPagingUiState = _movieListPagingUiState.asStateFlow()

    private fun eventMovieListPaging(event: MovieListPagingUiEvent) {
        _movieListPagingUiState.update { state ->
            reducerMovieListPaging(state = state, event = event)
        }
    }

    private fun reducerMovieListPaging(state: MovieListPagingUiState, event: MovieListPagingUiEvent): MovieListPagingUiState {
        return when(event) {
            is MovieListPagingUiEvent.UpdateMovieList -> {
                state.copy(movieList = MutableStateFlow(value = event.movieList!!))
            }
        }
    }

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
                .catch { e ->
                    _sideEffectEvent.send(SideEffectEvent.ShowToast(message = e.message?:""))
                }
                .collect {
                    eventMovieListPaging(event = MovieListPagingUiEvent.UpdateMovieList(movieList = it))
                }
        }
    }

    sealed interface SideEffectEvent {
        class ShowToast(val message: String): SideEffectEvent
    }

    override fun onCleared() {
        super.onCleared()

        _sideEffectEvent.cancel()
    }
}