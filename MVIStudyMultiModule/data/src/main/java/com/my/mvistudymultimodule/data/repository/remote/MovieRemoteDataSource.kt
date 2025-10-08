package com.my.mvistudymultimodule.data.repository.remote

import androidx.paging.PagingData
import com.my.mvistudymultimodule.core.model.MovieDetailModel
import com.my.mvistudymultimodule.core.model.MovieModel
import com.my.mvistudymultimodule.core.model.MovieReviewModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MovieRemoteDataSource {
    suspend fun getPopularMovie(language: String, page: Int): Response<MovieModel>
    suspend fun getPopularMoviePaging(language: String): Flow<PagingData<MovieModel.MovieModelResult>>
    suspend fun getSearchMovie(query: String, language: String, page: Int): Response<MovieModel>
    suspend fun getSearchMoviePaging(query: String, language: String): Flow<PagingData<MovieModel.MovieModelResult>>
    suspend fun getMovieDetail(movieId: Int, language: String): Response<MovieDetailModel>
    suspend fun getMovieReviewPaging(language: String, movieId: Int): Flow<PagingData<MovieReviewModel.Result>>
}