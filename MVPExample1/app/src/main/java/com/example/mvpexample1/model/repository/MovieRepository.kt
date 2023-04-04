package com.example.mvpexample1.model.repository

import com.example.mvpexample1.model.data.MovieModel
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    // remote
    suspend fun getSearchMovies(apiKey: String, query : String, language: String, page: Int) : Flow<MovieModel>

    // local
    suspend fun insertMovie(data: MovieModel.MovieModelResult)
    suspend fun getAllSavedMovies() : Flow<List<MovieModel.MovieModelResult>>
    suspend fun deleteMovie(data: MovieModel.MovieModelResult)
}