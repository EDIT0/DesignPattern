package com.my.mvistudymultimodule.feature.compose.moviedetail.event

sealed interface ComposeMovieDetailViewModelEvent {
    class GetMovieDetail(val movieId: Int): ComposeMovieDetailViewModelEvent
}