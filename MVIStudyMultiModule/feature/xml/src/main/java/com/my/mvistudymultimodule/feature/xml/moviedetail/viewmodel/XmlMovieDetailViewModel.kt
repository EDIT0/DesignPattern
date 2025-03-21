package com.my.mvistudymultimodule.feature.xml.moviedetail.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.my.mvistudymultimodule.core.base.BaseAndroidViewModel
import com.my.mvistudymultimodule.core.base.RequestResult
import com.my.mvistudymultimodule.domain.usecase.GetMovieDetailUseCase
import com.my.mvistudymultimodule.feature.xml.mainhome.event.MovieListErrorUiEvent
import com.my.mvistudymultimodule.feature.xml.mainhome.event.MovieListPagingUiEvent
import com.my.mvistudymultimodule.feature.xml.mainhome.state.MovieListPagingUiState
import com.my.mvistudymultimodule.feature.xml.moviedetail.event.MovieDetailErrorUiEvent
import com.my.mvistudymultimodule.feature.xml.moviedetail.event.MovieDetailUiEvent
import com.my.mvistudymultimodule.feature.xml.moviedetail.event.XmlMovieDetailViewModelEvent
import com.my.mvistudymultimodule.feature.xml.moviedetail.state.MovieDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
class XmlMovieDetailViewModel @Inject constructor(
    app: Application,
    private val getMovieDetailUseCase: GetMovieDetailUseCase
): BaseAndroidViewModel(app) {

    private val scope = viewModelScope
    private var scopeJob: Job? = null

    private val language = "ko-KR"
    private var movieId: Int = -1

    private var scrollPosition: Int = 0

    fun getScrollPosition(): Int = scrollPosition
    fun setScrollPosition(position: Int) {
        scrollPosition = position
    }

    private val _movieDetailUiEvent: MutableStateFlow<MovieDetailUiEvent> = MutableStateFlow(MovieDetailUiEvent.Idle)
    val movieDetailUiEvent: StateFlow<MovieDetailUiState> = _movieDetailUiEvent.runningFold(MovieDetailUiState()) { state, event ->
        when(event) {
            MovieDetailUiEvent.Idle -> {
                state
            }
            is MovieDetailUiEvent.UpdateLoading -> {
                state.copy(isLoading = event.isLoading)
            }
            is MovieDetailUiEvent.UpdateMovieDetail -> {
                state.copy(movieDetail = event.movieDetail)
            }
        }
    }.stateIn(scope, SharingStarted.Eagerly, MovieDetailUiState())

    private val _movieDetailErrorUiEvent = Channel<MovieDetailErrorUiEvent>()
    val movieDetailErrorUiEvent: Flow<MovieDetailErrorUiEvent> = _movieDetailErrorUiEvent.receiveAsFlow()

    fun handleViewModelEvent(xmlMovieDetailViewModelEvent: XmlMovieDetailViewModelEvent) {
        when(xmlMovieDetailViewModelEvent) {
            is XmlMovieDetailViewModelEvent.SetMovieId -> {
                movieId = xmlMovieDetailViewModelEvent.movieId
            }
            is XmlMovieDetailViewModelEvent.GetMovieDetail -> {
                getMovieDetail()
            }
        }
    }

    /**
     * 영화 상세정보 요청
     */
    private fun getMovieDetail() {
        scopeJob?.cancel()
        scopeJob = scope.launch {
            getMovieDetailUseCase.invoke(movieId, language)
                .onStart {
                    _movieDetailUiEvent.value = MovieDetailUiEvent.UpdateLoading(true)
                }
                .onCompletion {
                    _movieDetailUiEvent.value = MovieDetailUiEvent.UpdateLoading(false)
                }
                .filter {
                    val errorCode = "${it.code}"
                    val errorMessage = "${it.message}"

                    if(it is RequestResult.Error) {
                        _movieDetailErrorUiEvent.send(MovieDetailErrorUiEvent.Fail(errorCode, errorMessage))
                        when(errorCode) {
                            "ERROR" -> {
                            }
                        }
                    }

                    if(it is RequestResult.DataEmpty) {
                        _movieDetailErrorUiEvent.send(MovieDetailErrorUiEvent.DataEmpty(true))
                    }

                    return@filter it is RequestResult.Success
                }
                .catch { e ->
                    _movieDetailErrorUiEvent.send(MovieDetailErrorUiEvent.ExceptionHandle(e))
                }
                .map {
                    it.resultData
                }
                .collect {
                    delay(1000L)
                    _movieDetailUiEvent.value = MovieDetailUiEvent.UpdateMovieDetail(movieDetail = it)
                }
        }
    }
}