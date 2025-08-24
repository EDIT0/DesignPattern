package com.my.mvistudymultimodule.feature.compose.savedmovie.event

import androidx.paging.PagingData
import com.my.mvistudymultimodule.core.model.MovieDetailModel

/**
 * Screen 이벤트
 *
 */
sealed interface ComposeSavedMovieScreenEvent {
    class OnBackClick() : ComposeSavedMovieScreenEvent
    class OnMovieClick(val movieInfo: MovieDetailModel): ComposeSavedMovieScreenEvent
}

/**
 * ViewModel -> View 이벤트
 *
 */
sealed interface SavedMovieListPagingUiEvent {
    data class UpdateSavedMovieList(val savedMovieList: PagingData<MovieDetailModel>?) : SavedMovieListPagingUiEvent
}

/**
 * View -> ViewModel 이벤트
 *
 */
sealed interface ComposeSavedMovieViewModelEvent {
    class GetSavedMovie(): ComposeSavedMovieViewModelEvent
}