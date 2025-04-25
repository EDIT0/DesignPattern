package com.my.mvistudymultimodule.feature.compose.savedmovie.event

import com.my.mvistudymultimodule.core.model.MovieDetailModel

sealed interface ComposeSavedMovieHomeScreenEvent {
    class OnBackClick() : ComposeSavedMovieHomeScreenEvent
    class OnMovieClick(val movieInfo: MovieDetailModel): ComposeSavedMovieHomeScreenEvent
}