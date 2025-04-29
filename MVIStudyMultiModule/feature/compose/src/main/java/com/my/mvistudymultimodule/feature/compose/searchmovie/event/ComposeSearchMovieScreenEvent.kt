package com.my.mvistudymultimodule.feature.compose.searchmovie.event

import com.my.mvistudymultimodule.core.model.MovieModel

sealed interface ComposeSearchMovieScreenEvent {
    class OnBackClick() : ComposeSearchMovieScreenEvent
    class OnMovieClick(val movieInfo: MovieModel.MovieModelResult): ComposeSearchMovieScreenEvent
}