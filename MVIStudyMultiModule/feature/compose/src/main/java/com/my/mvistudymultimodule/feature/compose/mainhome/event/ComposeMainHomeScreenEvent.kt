package com.my.mvistudymultimodule.feature.compose.mainhome.event

import com.my.mvistudymultimodule.core.model.MovieModel

sealed interface ComposeMainHomeScreenEvent {
    class OnBackClick() : ComposeMainHomeScreenEvent
    class OnSearchClick(): ComposeMainHomeScreenEvent
    class OnMovieClick(val movieInfo: MovieModel.MovieModelResult): ComposeMainHomeScreenEvent
}