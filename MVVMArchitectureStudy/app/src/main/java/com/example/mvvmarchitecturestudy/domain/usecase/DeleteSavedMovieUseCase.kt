package com.example.mvvmarchitecturestudy.domain.usecase

import com.example.mvvmarchitecturestudy.data.model.MovieModelResult
import com.example.mvvmarchitecturestudy.domain.repository.MoviesRepository

class DeleteSavedMovieUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun execute(movieModelResult: MovieModelResult) {
        moviesRepository.deleteSavedMovie(movieModelResult)
    }
}