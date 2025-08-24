package com.my.mvistudymultimodule.feature.compose.searchmovie.event

import androidx.paging.PagingData
import com.my.mvistudymultimodule.core.model.MovieModel

/**
 * Screen 이벤트
 *
 */
sealed interface ComposeSearchMovieScreenEvent {
    class OnBackClick() : ComposeSearchMovieScreenEvent
    class OnMovieClick(val movieInfo: MovieModel.MovieModelResult): ComposeSearchMovieScreenEvent
}

/**
 * ViewModel -> View 이벤트
 *
 */
sealed interface SearchStateUiEvent {
    data class UpdateSearchKeyword(val searchKeyword: String = ""): SearchStateUiEvent
    data class UpdateSearchMovieList(val searchMovieList: PagingData<MovieModel.MovieModelResult>?): SearchStateUiEvent
}

/**
 * View -> ViewModel 이벤트
 *
 */
sealed interface ComposeSearchMovieViewModelEvent {

    class GetSearchMovie(): ComposeSearchMovieViewModelEvent
    class SaveCurrentSearchKeyword(
        val searchKeyword: String, // 검색 키워드
        val isDirectSearch: Boolean // true: 바로 검색, false: 지연 검색
    ): ComposeSearchMovieViewModelEvent
}