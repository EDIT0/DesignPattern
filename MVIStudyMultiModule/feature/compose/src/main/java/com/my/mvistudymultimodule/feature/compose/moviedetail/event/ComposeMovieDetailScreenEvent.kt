package com.my.mvistudymultimodule.feature.compose.moviedetail.event

sealed interface ComposeMovieDetailScreenEvent {
    class OnBackClick() : ComposeMovieDetailScreenEvent
}