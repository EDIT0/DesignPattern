package com.example.mvvmarchitecturestudy2.domain.usecase

import com.example.mvvmarchitecturestudy2.data.model.MovieModel
import com.example.mvvmarchitecturestudy2.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetSearchMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    suspend fun execute(query : String, language: String, page: Int): Flow<MovieModel> {
        return movieRepository.getSearchMovies(query, language, page)
    }
}