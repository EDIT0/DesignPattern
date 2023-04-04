package com.example.mvvmarchitecturestudy2.domain.usecase

import com.example.mvvmarchitecturestudy2.data.model.MovieModel
import com.example.mvvmarchitecturestudy2.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetPopularMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    suspend fun execute(language: String, page: Int): Flow<MovieModel> {
        return movieRepository.getPopularMovies(language, page)
    }
}