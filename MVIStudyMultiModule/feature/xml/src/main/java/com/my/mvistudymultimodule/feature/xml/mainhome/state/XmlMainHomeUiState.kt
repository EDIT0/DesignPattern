package com.my.mvistudymultimodule.feature.xml.mainhome.state

import com.my.mvistudymultimodule.core.model.MovieModel

data class MovieListUiState(
    val movieList: List<MovieModel.MovieModelResult>? = null,
    val isLoading: Boolean = false
)