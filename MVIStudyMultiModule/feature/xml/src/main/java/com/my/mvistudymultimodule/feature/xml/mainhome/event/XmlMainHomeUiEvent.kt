package com.my.mvistudymultimodule.feature.xml.mainhome.event

import com.my.mvistudymultimodule.core.model.MovieModel

sealed interface MovieListUiEvent {
    object Idle : MovieListUiEvent
    data class UpdateMovieList(val movieList: List<MovieModel.MovieModelResult>?) : MovieListUiEvent
    data class UpdateLoading(val isLoading: Boolean) : MovieListUiEvent
}

sealed interface MovieListErrorUiEvent {
    class Idle : MovieListErrorUiEvent
    class Fail(val code: String, val message: String?) : MovieListErrorUiEvent
    class ExceptionHandle(val throwable: Throwable) : MovieListErrorUiEvent
    class DataEmpty(val isDataEmpty: Boolean) : MovieListErrorUiEvent
    class ConnectionError(val code: String, val message: String?) : MovieListErrorUiEvent
}