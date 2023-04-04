package com.example.mvpexample1.model.usecase

import androidx.lifecycle.LiveData
import com.example.mvpexample1.model.data.MovieModel
import com.example.mvpexample1.model.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetAllSavedMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    suspend fun execute() : Flow<List<MovieModel.MovieModelResult>> {
        return movieRepository.getAllSavedMovies()
    }
}