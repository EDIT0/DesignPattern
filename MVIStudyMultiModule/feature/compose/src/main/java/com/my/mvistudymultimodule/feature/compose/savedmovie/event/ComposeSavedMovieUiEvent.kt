package com.my.mvistudymultimodule.feature.compose.savedmovie.event

import androidx.paging.PagingData
import com.my.mvistudymultimodule.core.model.MovieDetailModel

sealed interface SavedMovieListPagingUiEvent {
    object Idle : SavedMovieListPagingUiEvent
    data class UpdateSavedMovieList(val savedMovieList: PagingData<MovieDetailModel>?) : SavedMovieListPagingUiEvent
}