package com.example.mvpexample1.model.repository.remote

import com.example.mvpexample1.model.data.MovieModel
import com.example.mvpexample1.model.network.ApiService
import retrofit2.Response

class MovieRemoteDataSourceImpl(
    private val apiService: ApiService
) : MovieRemoteDataSource {
    override suspend fun getSearchMovies(apiKey: String, query: String, language: String, page: Int): Response<MovieModel> {
        return apiService.getSearchMovies(apiKey, query, language, page)
    }
}