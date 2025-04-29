package com.my.mvistudymultimodule.feature.compose.searchmovie.event

import androidx.paging.PagingData
import com.my.mvistudymultimodule.core.model.MovieModel

sealed interface SearchStateUiEvent {
    object Idle: SearchStateUiEvent
    data class UpdateSearchKeyword(val searchKeyword: String = ""): SearchStateUiEvent
    data class UpdateSearchMovieList(val searchMovieList: PagingData<MovieModel.MovieModelResult>?): SearchStateUiEvent
}