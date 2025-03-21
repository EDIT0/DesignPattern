package com.my.mvistudymultimodule.domain.repository

import androidx.paging.PagingData
import com.my.mvistudymultimodule.core.base.RequestResult
import com.my.mvistudymultimodule.core.model.MovieDetailModel
import com.my.mvistudymultimodule.core.model.MovieModel
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovie(language: String, page: Int): Flow<RequestResult<MovieModel>>
    suspend fun getPopularMoviePaging(language: String): Flow<PagingData<MovieModel. MovieModelResult>>
    suspend fun getSearchMovie(query: String, language: String, page: Int): Flow<RequestResult<MovieModel>>
    suspend fun getMovieDetail(movieId: Int, language: String): Flow<RequestResult<MovieDetailModel>>
}