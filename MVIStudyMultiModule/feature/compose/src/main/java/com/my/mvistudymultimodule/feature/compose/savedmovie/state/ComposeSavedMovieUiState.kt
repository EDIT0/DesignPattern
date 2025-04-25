package com.my.mvistudymultimodule.feature.compose.savedmovie.state

import androidx.paging.PagingData
import com.my.mvistudymultimodule.core.model.MovieDetailModel
import kotlinx.coroutines.flow.MutableStateFlow

data class SavedMovieListPagingUiState(
    val savedMovieList: MutableStateFlow<PagingData<MovieDetailModel>>? = null,
)