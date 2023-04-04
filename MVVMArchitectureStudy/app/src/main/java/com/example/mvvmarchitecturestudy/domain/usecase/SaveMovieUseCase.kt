package com.example.mvvmarchitecturestudy.domain.usecase

import com.example.mvvmarchitecturestudy.data.model.MovieModelResult
import com.example.mvvmarchitecturestudy.domain.repository.MoviesRepository

class SaveMovieUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun execute(movieModelResult: MovieModelResult) {
        return moviesRepository.saveMovie(movieModelResult)
    }
}