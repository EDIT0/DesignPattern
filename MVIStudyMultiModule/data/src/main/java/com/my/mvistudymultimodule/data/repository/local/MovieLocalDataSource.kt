package com.my.mvistudymultimodule.data.repository.local

import com.my.mvistudymultimodule.core.model.MovieDetailModel


interface MovieLocalDataSource {
    suspend fun saveMovieDetail(movieDetail: MovieDetailModel): Boolean
    suspend fun deleteMovieDetail(movieDetail: MovieDetailModel): Boolean
    suspend fun checkMovieDetail(movieId: Int): Boolean
}