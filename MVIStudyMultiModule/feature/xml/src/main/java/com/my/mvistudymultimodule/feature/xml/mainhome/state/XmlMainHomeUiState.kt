package com.my.mvistudymultimodule.feature.xml.mainhome.state

import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import com.my.mvistudymultimodule.core.model.MovieModel

data class MovieListUiState(
    val movieList: List<MovieModel.MovieModelResult>? = null,
    val isLoading: Boolean = false
)

data class MovieListPagingUiState(
    val movieList: PagingData<MovieModel.MovieModelResult>? = null,
    val loadStates: CombinedLoadStates? = null
)