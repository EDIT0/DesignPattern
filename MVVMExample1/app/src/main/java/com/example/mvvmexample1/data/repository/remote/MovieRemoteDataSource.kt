package com.example.mvvmexample1.data.repository.remote

import com.example.mvvmexample1.data.model.MovieModel
import retrofit2.Response

interface MovieRemoteDataSource {
    suspend fun getSearchMovies(apiKey: String, query : String, language: String, page: Int) : Response<MovieModel>
}