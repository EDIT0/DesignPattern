package com.my.mvistudymultimodule.feature.compose.searchmovie.state

import androidx.paging.PagingData
import com.my.mvistudymultimodule.core.model.MovieModel
import kotlinx.coroutines.flow.MutableStateFlow

data class SearchUiState(
    val searchKeyword: String? = "",
    val searchMovieList: MutableStateFlow<PagingData<MovieModel.MovieModelResult>> = MutableStateFlow(PagingData.empty())
)