package com.my.mvistudymultimodule.feature.compose.moviedetail.event

import com.my.mvistudymultimodule.core.model.MovieModel

sealed interface ComposeMovieDetailViewModelEvent {
    class GetMovieDetail(val movieId: Int, val movieInfo: MovieModel.MovieModelResult): ComposeMovieDetailViewModelEvent
}