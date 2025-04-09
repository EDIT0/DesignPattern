package com.my.mvistudymultimodule.data.repository.local

import com.my.mvistudymultimodule.core.database.dao.MovieDetailDao
import com.my.mvistudymultimodule.core.model.MovieDetailModel
import javax.inject.Inject

class MovieLocalDataSourceImpl @Inject constructor(
    private val movieDetailDao: MovieDetailDao
) : MovieLocalDataSource {
    override suspend fun saveMovieDetail(movieDetail: MovieDetailModel): Boolean {
        val result = movieDetailDao.insertMovieDetail(movieDetail = movieDetail)
        return result > 0L
    }

    override suspend fun deleteMovieDetail(movieDetail: MovieDetailModel): Boolean {
        val result = movieDetailDao.deleteMovieDetail(movieDetail = movieDetail)
        return (result > 0)
    }

    override suspend fun checkMovieDetail(movieId: Int): Boolean {
        return movieDetailDao.checkMovieDetail(movieId = movieId)
    }

}