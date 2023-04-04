package com.example.mvvmexample1.domain.repository

import com.example.mvvmexample1.data.model.MovieModel
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    // remote
    suspend fun getSearchMovies(apiKey: String, query : String, language: String, page: Int) : Flow<MovieModel>

    // local
    suspend fun insertMovie(data: MovieModel.MovieModelResult)
    suspend fun getAllSavedMovies() : Flow<List<MovieModel.MovieModelResult>>
    suspend fun deleteMovie(data: MovieModel.MovieModelResult)
}