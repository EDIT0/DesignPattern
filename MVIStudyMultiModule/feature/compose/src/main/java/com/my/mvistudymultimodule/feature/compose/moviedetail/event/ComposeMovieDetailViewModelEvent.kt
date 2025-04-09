package com.my.mvistudymultimodule.feature.compose.moviedetail.event

import com.my.mvistudymultimodule.core.model.MovieDetailModel
import com.my.mvistudymultimodule.core.model.MovieModel

sealed interface ComposeMovieDetailViewModelEvent {
    class GetMovieDetail(val movieId: Int, val movieInfo: MovieModel.MovieModelResult): ComposeMovieDetailViewModelEvent
    class SaveMovieDetail(val movieDetail: MovieDetailModel): ComposeMovieDetailViewModelEvent
    class DeleteMovieDetail(val movieDetail: MovieDetailModel): ComposeMovieDetailViewModelEvent
}