package com.my.mvistudymultimodule.feature.compose.searchmovie.event

sealed interface ComposeSearchMovieViewModelEvent {

    class GetSearchMovie(): ComposeSearchMovieViewModelEvent
    class SaveCurrentSearchKeyword(
        val searchKeyword: String, // 검색 키워드
        val isDirectSearch: Boolean // true: 바로 검색, false: 지연 검색
    ): ComposeSearchMovieViewModelEvent
}