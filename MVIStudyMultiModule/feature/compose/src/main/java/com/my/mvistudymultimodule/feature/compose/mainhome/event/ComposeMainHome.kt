package com.my.mvistudymultimodule.feature.compose.mainhome.event

import androidx.paging.PagingData
import com.my.mvistudymultimodule.core.model.MovieModel

/**
 * Screen 이벤트
 *
 */
sealed interface ComposeMainHomeScreenEvent {
    class OnBackClick() : ComposeMainHomeScreenEvent
    class OnSearchClick(): ComposeMainHomeScreenEvent
    class OnMovieClick(val movieInfo: MovieModel.MovieModelResult): ComposeMainHomeScreenEvent
    class OnSavedMovieFloatingButtonClick(): ComposeMainHomeScreenEvent
}

/**
 * ViewModel -> View 이벤트
 *
 */
sealed interface MovieListUiEvent {
    data class UpdateMovieList(val movieList: List<MovieModel.MovieModelResult>?) : MovieListUiEvent
    data class UpdateLoading(val isLoading: Boolean) : MovieListUiEvent
}

sealed interface MovieListPagingUiEvent {
    data class UpdateMovieList(val movieList: PagingData<MovieModel.MovieModelResult>?) : MovieListPagingUiEvent
}

/**
 * View -> ViewModel 이벤트
 *
 */
sealed interface ComposeMainHomeViewModelEvent {
    class GetPopularMovie(): ComposeMainHomeViewModelEvent
}