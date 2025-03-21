package com.my.mvistudymultimodule.data.repository

import androidx.paging.PagingData
import com.my.mvistudymultimodule.core.base.RequestResult
import com.my.mvistudymultimodule.core.model.MovieDetailModel
import com.my.mvistudymultimodule.core.model.MovieModel
import com.my.mvistudymultimodule.data.repository.remote.MovieRemoteDataSource
import com.my.mvistudymultimodule.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource,
): MovieRepository {
    override suspend fun getPopularMovie(
        language: String,
        page: Int
    ): Flow<RequestResult<MovieModel>> {
        return flow<RequestResult<MovieModel>> {
            val response = movieRemoteDataSource.getPopularMovie(language, page)

            if(response.isSuccessful) {
                if(response.body()?.movieModelResults?.isEmpty() == true) {
                    emit(RequestResult.DataEmpty())
                } else {
                    response.body()?.let {
                        emit(RequestResult.Success(it))
                    }
                }
            } else {
                emit(RequestResult.Error("ERROR", "error message"))
            }
        }.catch {
            throw Exception(it)
        }
    }

    override suspend fun getPopularMoviePaging(
        language: String
    ): Flow<PagingData<MovieModel.MovieModelResult>> {
        return movieRemoteDataSource.getPopularMoviePaging(language)
    }

    override suspend fun getSearchMovie(
        query: String,
        language: String,
        page: Int
    ): Flow<RequestResult<MovieModel>> {
        return flow<RequestResult<MovieModel>> {
            val response = movieRemoteDataSource.getSearchMovie(query, language, page)

            if(response.isSuccessful) {
                if(response.body()?.movieModelResults?.isEmpty() == true) {
                    emit(RequestResult.DataEmpty())
                } else {
                    response.body()?.let {
                        emit(RequestResult.Success(it))
                    }
                }
            } else {
                emit(RequestResult.Error("ERROR", "error message"))
            }
        }.catch {
            throw Exception(it)
        }
    }

    override suspend fun getMovieDetail(
        movieId: Int,
        language: String
    ): Flow<RequestResult<MovieDetailModel>> {
        return flow<RequestResult<MovieDetailModel>> {
            val response = movieRemoteDataSource.getMovieDetail(movieId, language)

            if(response.isSuccessful) {
                if(response.body() == null) {
                    emit(RequestResult.DataEmpty())
                } else {
                    response.body()?.let {
                        emit(RequestResult.Success(it))
                    }
                }
            } else {
                emit(RequestResult.Error("ERROR", "error message"))
            }
        }.catch {
            throw Exception(it)
        }
    }
}