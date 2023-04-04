package com.example.mvvmexample1.domain.usecase

import com.example.mvvmexample1.data.model.MovieModel
import com.example.mvvmexample1.domain.repository.MovieRepository


class DeleteMovieUseCase(
    private val movieRepository: MovieRepository
) {
    suspend fun execute(data: MovieModel.MovieModelResult) {
        movieRepository.deleteMovie(data)
    }
}