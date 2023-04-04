package com.example.mvpexample1.model.usecase

import com.example.mvpexample1.model.data.MovieModel
import com.example.mvpexample1.model.repository.MovieRepository

class DeleteMovieUseCase(
    private val movieRepository: MovieRepository
) {
    suspend fun execute(data: MovieModel.MovieModelResult) {
        movieRepository.deleteMovie(data)
    }
}