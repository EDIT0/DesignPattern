package com.example.mvvmarchitecturestudy2.data.repository

import com.example.mvvmarchitecturestudy2.data.model.MovieDetailModel
import com.example.mvvmarchitecturestudy2.data.model.MovieModel
import com.example.mvvmarchitecturestudy2.data.model.MovieReviewModel
import com.example.mvvmarchitecturestudy2.data.repository.remote.MovieRemoteDataSource
import com.example.mvvmarchitecturestudy2.data.util.ERROR
import com.example.mvvmarchitecturestudy2.data.util.LAST_PAGE
import com.example.mvvmarchitecturestudy2.data.util.NO_DATA
import com.example.mvvmarchitecturestudy2.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class MovieRepositoryImpl(
    private val movieRemoteDataSource: MovieRemoteDataSource
) : MovieRepository{
    override suspend fun getPopularMovies(language: String, page: Int): Flow<MovieModel> {
        return flow {
            val response = movieRemoteDataSource.getPopularMovies(language, page)

            if(response.isSuccessful) {
                if(response.body()?.movieModelResults?.isEmpty() == true) {
                    throw Exception(NO_DATA)
                } else {
                    response.body()?.let {
                        emit(it)
                    }
                }
            } else {
                throw Exception(ERROR)
            }
        }
    }

    override suspend fun getSearchMovies(
        query: String,
        language: String,
        page: Int
    ): Flow<MovieModel> {
        return flow {
            val response = movieRemoteDataSource.getSearchMovies(query, language, page)

            if(response.isSuccessful) {
                if(response.body()?.movieModelResults?.isEmpty() == true) {
                    throw Exception(NO_DATA)
                } else {
                    response.body()?.let {
                        emit(it)
                    }
                }
            } else {
                throw Exception(ERROR)
            }
        }
    }

    override suspend fun getMovieDetail(movieId: Int, language: String): Flow<MovieDetailModel> {
        return flow {
            val response = movieRemoteDataSource.getMovieDetail(movieId, language)

            if(response.isSuccessful) {
                if(response.body()?.id == null) {
                    throw Exception(NO_DATA)
                } else {
                    response.body()?.let {
                        emit(it)
                    }
                }
            } else {
                throw Exception(ERROR)
            }
        }
    }

    override suspend fun getMovieReview(
        movieId: Int,
        language: String,
        page: Int
    ): Flow<MovieReviewModel> {
        return flow {
            val response = movieRemoteDataSource.getMovieReview(movieId, language, page)

            if(response.isSuccessful) {
                if(response.body()?.id == null) {
                    throw Exception(NO_DATA)
                } else {
                    response.body()?.let {
                        emit(it)
                    }
                }
            } else {
                throw Exception(ERROR)
            }
        }
    }

}