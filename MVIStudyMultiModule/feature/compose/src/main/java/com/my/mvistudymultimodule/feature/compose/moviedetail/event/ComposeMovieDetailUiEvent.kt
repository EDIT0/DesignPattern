package com.my.mvistudymultimodule.feature.compose.moviedetail.event

import com.my.mvistudymultimodule.core.model.MovieDetailModel

sealed interface MovieDetailUiEvent {
    object Idle: MovieDetailUiEvent
    data class UpdateMovieDetail(val movieDetail: MovieDetailModel?): MovieDetailUiEvent
    data class UpdateLoading(val isLoading: Boolean) : MovieDetailUiEvent
    data class UpdateSaveState(val isSaveState: Boolean): MovieDetailUiEvent
}

sealed interface MovieDetailErrorUiEvent {
    class Idle : MovieDetailErrorUiEvent
    class Fail(val code: String, val message: String?) : MovieDetailErrorUiEvent
    class ExceptionHandle(val throwable: Throwable) : MovieDetailErrorUiEvent
    class DataEmpty(val isDataEmpty: Boolean) : MovieDetailErrorUiEvent
    class ConnectionError(val code: String, val message: String?) : MovieDetailErrorUiEvent
}

sealed interface SaveMovieDetailErrorUiEvent {
    class Idle : SaveMovieDetailErrorUiEvent
    class Fail(val code: String, val message: String?) : SaveMovieDetailErrorUiEvent
    class ExceptionHandle(val throwable: Throwable) : SaveMovieDetailErrorUiEvent
    class DataEmpty(val isDataEmpty: Boolean) : SaveMovieDetailErrorUiEvent
    class ConnectionError(val code: String, val message: String?) : SaveMovieDetailErrorUiEvent
}