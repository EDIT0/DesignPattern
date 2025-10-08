package com.my.mvistudymultimodule.feature.compose.moviedetail.state

import androidx.paging.PagingData
import com.my.mvistudymultimodule.core.model.MovieDetailModel
import com.my.mvistudymultimodule.core.model.MovieReviewModel
import kotlinx.coroutines.flow.MutableStateFlow

data class MovieDetailUiState(
    val movieDetail: MovieDetailModel? = null,
    val isLoading: Boolean = false,
    val isSaveState: Boolean = false
)

data class MovieReviewListPagingUiState(
    val reviewList: MutableStateFlow<PagingData<MovieReviewModel.Result>>? = null
)