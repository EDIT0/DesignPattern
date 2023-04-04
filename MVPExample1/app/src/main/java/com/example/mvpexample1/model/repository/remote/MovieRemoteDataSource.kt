package com.example.mvpexample1.model.repository.remote

import com.example.mvpexample1.model.data.MovieModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MovieRemoteDataSource {
    suspend fun getSearchMovies(apiKey: String, query : String, language: String, page: Int) : Response<MovieModel>
}