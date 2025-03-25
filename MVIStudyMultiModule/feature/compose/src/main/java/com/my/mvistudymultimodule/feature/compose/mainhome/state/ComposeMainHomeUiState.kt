package com.my.mvistudymultimodule.feature.compose.mainhome.state

import androidx.paging.PagingData
import com.my.mvistudymultimodule.core.model.MovieModel
import kotlinx.coroutines.flow.MutableStateFlow

data class MovieListUiState(
    val movieList: List<MovieModel.MovieModelResult>? = null,
    val isLoading: Boolean = false
)

data class MovieListPagingUiState(
    val movieList: MutableStateFlow<PagingData<MovieModel.MovieModelResult>>? = null,
)