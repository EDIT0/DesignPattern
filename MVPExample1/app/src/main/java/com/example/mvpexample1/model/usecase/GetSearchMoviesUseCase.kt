package com.example.mvpexample1.model.usecase

import com.example.mvpexample1.model.data.MovieModel
import com.example.mvpexample1.model.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetSearchMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    suspend fun execute(apiKey: String, query : String, language: String, page: Int): Flow<MovieModel> {
        return movieRepository.getSearchMovies(apiKey, query, language, page)
    }
}