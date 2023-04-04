package com.example.mvvmexample1.domain.usecase

import com.example.mvvmexample1.data.model.MovieModel
import com.example.mvvmexample1.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetAllSavedMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    suspend fun execute() : Flow<List<MovieModel.MovieModelResult>> {
        return movieRepository.getAllSavedMovies()
    }
}