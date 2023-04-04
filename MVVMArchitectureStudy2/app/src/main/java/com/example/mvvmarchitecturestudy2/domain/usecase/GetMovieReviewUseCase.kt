package com.example.mvvmarchitecturestudy2.domain.usecase

import com.example.mvvmarchitecturestudy2.data.model.MovieDetailModel
import com.example.mvvmarchitecturestudy2.data.model.MovieReviewModel
import com.example.mvvmarchitecturestudy2.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetMovieReviewUseCase(
    private val movieRepository: MovieRepository
) {
    suspend fun execute(movieId: Int, language: String, page: Int) : Flow<MovieReviewModel> {
        return movieRepository.getMovieReview(movieId, language, page)
    }
}