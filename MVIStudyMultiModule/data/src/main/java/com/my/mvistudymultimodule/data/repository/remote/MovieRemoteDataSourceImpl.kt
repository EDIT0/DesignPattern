package com.my.mvistudymultimodule.data.repository.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.my.mvistudymultimodule.core.model.MovieDetailModel
import com.my.mvistudymultimodule.core.model.MovieModel
import com.my.mvistudymultimodule.core.model.MovieReviewModel
import com.my.mvistudymultimodule.data.BuildConfig
import com.my.mvistudymultimodule.data.api.ApiService
import com.my.mvistudymultimodule.data.repository.remote.paging.GetMovieReviewPagingSource
import com.my.mvistudymultimodule.data.repository.remote.paging.GetPopularMoviePagingSource
import com.my.mvistudymultimodule.data.repository.remote.paging.GetSearchMoviePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import retrofit2.Response
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : MovieRemoteDataSource {
    override suspend fun getPopularMovie(
        language: String,
        page: Int
    ): Response<MovieModel> {
        return apiService.getPopularMovie(BuildConfig.API_KEY, language, page)
    }

    override suspend fun getPopularMoviePaging(
        language: String
    ): Flow<PagingData<MovieModel.MovieModelResult>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 10,
                enablePlaceholders = false,
                initialLoadSize = 60,
                maxSize = 10000,
//                jumpThreshold =
            ),

            // 사용할 메소드 선언
            pagingSourceFactory = {
                GetPopularMoviePagingSource(apiService, language)
            }
        ).flow.catch {
            throw Exception(it)
        }
    }

    override suspend fun getSearchMovie(
        query: String,
        language: String,
        page: Int
    ): Response<MovieModel> {
        return apiService.getSearchMovie(BuildConfig.API_KEY, query, language, page)
    }

    override suspend fun getSearchMoviePaging(
        query: String,
        language: String
    ): Flow<PagingData<MovieModel.MovieModelResult>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 10,
                maxSize = 10000,
//                jumpThreshold = TODO()
            ),
            pagingSourceFactory = {
                GetSearchMoviePagingSource(apiService, query, language)
            }
        ).flow.catch {
            throw Exception(it)
        }
    }

    override suspend fun getMovieDetail(
        movieId: Int,
        language: String
    ): Response<MovieDetailModel> {
        return apiService.getMovieDetail(movieId, BuildConfig.API_KEY, language)
    }

    override suspend fun getMovieReviewPaging(
        language: String,
        movieId: Int
    ): Flow<PagingData<MovieReviewModel.Result>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 10,
                maxSize = 10000,
//                jumpThreshold = TODO()
            ),
            pagingSourceFactory = {
                GetMovieReviewPagingSource(
                    apiService = apiService,
                    language = language,
                    movieId = movieId
                )
            }
        ).flow.catch {
            throw Exception(it)
        }
    }
}