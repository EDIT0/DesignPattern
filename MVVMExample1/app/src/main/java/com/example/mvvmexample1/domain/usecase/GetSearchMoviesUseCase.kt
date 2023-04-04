package com.example.mvvmexample1.domain.usecase

import com.example.mvvmexample1.data.model.MovieModel
import com.example.mvvmexample1.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetSearchMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    suspend fun execute(apiKey: String, query : String, language: String, page: Int): Flow<MovieModel> {
        return movieRepository.getSearchMovies(apiKey, query, language, page)
    }
}