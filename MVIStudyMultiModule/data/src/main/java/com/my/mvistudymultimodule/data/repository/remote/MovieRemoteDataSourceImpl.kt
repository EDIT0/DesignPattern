package com.my.mvistudymultimodule.data.repository.remote

import com.my.mvistudymultimodule.core.model.MovieModel
import com.my.mvistudymultimodule.data.BuildConfig
import com.my.mvistudymultimodule.data.api.ApiService
import retrofit2.Response
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : MovieRemoteDataSource {
    override suspend fun getPopularMovie(language: String, page: Int): Response<MovieModel> {
        return apiService.getPopularMovies(BuildConfig.API_KEY, language, page)
    }

    override suspend fun getSearchMovie(query: String, language: String, page: Int): Response<MovieModel> {
        return apiService.getSearchMovies(BuildConfig.API_KEY, query, language, page)
    }
}