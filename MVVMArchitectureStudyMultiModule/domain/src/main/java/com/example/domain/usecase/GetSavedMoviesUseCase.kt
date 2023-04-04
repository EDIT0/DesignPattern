package com.example.domain.usecase

import com.example.domain.model.MovieModelResult
import com.example.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

class GetSavedMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {
    fun execute() : Flow<List<MovieModelResult>>{
        return moviesRepository.getSavedMovies()
    }
}