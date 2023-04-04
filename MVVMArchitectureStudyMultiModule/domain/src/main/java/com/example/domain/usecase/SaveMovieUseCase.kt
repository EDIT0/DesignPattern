package com.example.domain.usecase

import com.example.domain.repository.MoviesRepository
import com.example.domain.model.MovieModelResult

class SaveMovieUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun execute(movieModelResult: MovieModelResult) {
        return moviesRepository.saveMovie(movieModelResult)
    }
}