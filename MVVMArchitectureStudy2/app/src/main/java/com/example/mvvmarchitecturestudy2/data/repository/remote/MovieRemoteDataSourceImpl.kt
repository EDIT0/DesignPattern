package com.example.mvvmarchitecturestudy2.data.repository.remote

import com.example.mvvmarchitecturestudy2.BuildConfig
import com.example.mvvmarchitecturestudy2.data.api.TMDBAPIService
import com.example.mvvmarchitecturestudy2.data.model.MovieDetailModel
import com.example.mvvmarchitecturestudy2.data.model.MovieModel
import com.example.mvvmarchitecturestudy2.data.model.MovieReviewModel
import retrofit2.Response

class MovieRemoteDataSourceImpl(
    private val tmdbApiService: TMDBAPIService
) : MovieRemoteDataSource{
    override suspend fun getPopularMovies(language: String, page: Int): Response<MovieModel> {
        return tmdbApiService.getPopularMovies(BuildConfig.API_KEY, language, page)
    }

    override suspend fun getSearchMovies(
        query: String,
        language: String,
        page: Int
    ): Response<MovieModel> {
        return tmdbApiService.getSearchMovies(BuildConfig.API_KEY, query, language, page, false)
    }

    override suspend fun getMovieDetail(
        movieId: Int,
        language: String
    ): Response<MovieDetailModel> {
        return tmdbApiService.getMovieDetail(movieId, BuildConfig.API_KEY, language)
    }

    override suspend fun getMovieReview(
        movieId: Int,
        language: String,
        page: Int
    ): Response<MovieReviewModel> {
        return tmdbApiService.getMovieReview(movieId, BuildConfig.API_KEY, language, page)
    }

}