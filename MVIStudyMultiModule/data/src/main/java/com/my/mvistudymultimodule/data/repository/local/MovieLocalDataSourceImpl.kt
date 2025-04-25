package com.my.mvistudymultimodule.data.repository.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.my.mvistudymultimodule.core.database.dao.MovieDetailDao
import com.my.mvistudymultimodule.core.model.MovieDetailModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
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

    override fun getSavedMoviePaging(): Flow<PagingData<MovieDetailModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 10,
                enablePlaceholders = false,
                initialLoadSize = 20,
                maxSize = 10000,
//                jumpThreshold =
            ),

            // 사용할 메소드 선언
            pagingSourceFactory = {
                movieDetailDao.getSavedMoviePaging()
            }
        ).flow
            .map { pagingData ->
                delay(1000L)
                pagingData
            }
            .catch {
                throw Exception(it)
            }
    }

}