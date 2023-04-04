package com.example.mvvmarchitecturestudy.data.repository.remoteDataSourceImpl

import com.example.mvvmarchitecturestudy.BuildConfig
import com.example.mvvmarchitecturestudy.data.api.TmdbAPIService
import com.example.mvvmarchitecturestudy.data.model.MovieModel
import com.example.mvvmarchitecturestudy.data.repository.remoteDataSource.MoviesRemoteDataSource
import retrofit2.Response

class MoviesRemoteDataSourceImpl(
    private val tmdbAPIService: TmdbAPIService
) : MoviesRemoteDataSource {
    override suspend fun getPopularMovies(language: String, page: Int): Response<MovieModel> {
        return tmdbAPIService.getPopularMovies(BuildConfig.API_KEY, language, page)
    }

    override suspend fun getSearchMovies(query: String, language: String, page: Int): Response<MovieModel> {
        return tmdbAPIService.getSearchMovies(BuildConfig.API_KEY, query, language, page)
    }
}