package com.example.mvpexample1.model.repository.local

import com.example.mvpexample1.model.data.MovieModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MovieLocalDataSource {
    suspend fun insertMovie(data: MovieModel.MovieModelResult)
    suspend fun getAllSavedMovies() : Flow<List<MovieModel.MovieModelResult>>
    suspend fun deleteMovie(data: MovieModel.MovieModelResult)
}