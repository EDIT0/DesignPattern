package com.example.mvvmexample1.data.repository.local

import com.example.mvvmexample1.data.model.MovieModel
import kotlinx.coroutines.flow.Flow

interface MovieLocalDataSource {
    suspend fun insertMovie(data: MovieModel.MovieModelResult)
    suspend fun getAllSavedMovies() : Flow<List<MovieModel.MovieModelResult>>
    suspend fun deleteMovie(data: MovieModel.MovieModelResult)
}