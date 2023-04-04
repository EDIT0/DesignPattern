package com.example.domain.usecase

import com.example.domain.model.MovieModelResult
import com.example.domain.repository.MoviesRepository

class DeleteSavedMovieUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun execute(movieModelResult: MovieModelResult) {
        moviesRepository.deleteSavedMovie(movieModelResult)
    }
}