package com.my.mvistudymultimodule.data.repository.local

import androidx.paging.PagingData
import com.my.mvistudymultimodule.core.model.MovieDetailModel
import kotlinx.coroutines.flow.Flow


interface MovieLocalDataSource {
    suspend fun saveMovieDetail(movieDetail: MovieDetailModel): Boolean
    suspend fun deleteMovieDetail(movieDetail: MovieDetailModel): Boolean
    suspend fun checkMovieDetail(movieId: Int): Boolean
    fun getSavedMoviePaging(): Flow<PagingData<MovieDetailModel>>
}