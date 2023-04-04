package com.example.mvvmexample1.data.repository.remote

import com.example.mvvmexample1.data.model.MovieModel
import com.example.mvvmexample1.data.network.ApiService
import retrofit2.Response

class MovieRemoteDataSourceImpl(
    private val apiService: ApiService
) : MovieRemoteDataSource {
    override suspend fun getSearchMovies(apiKey: String, query: String, language: String, page: Int): Response<MovieModel> {
        return apiService.getSearchMovies(apiKey, query, language, page)
    }
}