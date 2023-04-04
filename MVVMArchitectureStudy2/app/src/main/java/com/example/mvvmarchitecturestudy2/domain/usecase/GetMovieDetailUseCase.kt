package com.example.mvvmarchitecturestudy2.domain.usecase

import com.example.mvvmarchitecturestudy2.data.model.MovieDetailModel
import com.example.mvvmarchitecturestudy2.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetMovieDetailUseCase(
    private val movieRepository: MovieRepository
) {
    suspend fun execute(movieId: Int, language: String) : Flow<MovieDetailModel> {
        return movieRepository.getMovieDetail(movieId, language)
    }
}