package com.my.mvistudymultimodule.feature.compose.moviedetail.state

import com.my.mvistudymultimodule.core.model.MovieDetailModel

data class MovieDetailUiState(
    val movieDetail: MovieDetailModel? = null,
    val isLoading: Boolean = false
)