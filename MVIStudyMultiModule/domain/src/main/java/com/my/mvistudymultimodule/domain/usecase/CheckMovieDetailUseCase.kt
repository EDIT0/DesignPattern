package com.my.mvistudymultimodule.domain.usecase

import com.my.mvistudymultimodule.core.base.RequestResult
import com.my.mvistudymultimodule.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckMovieDetailUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend fun invoke(movieId: Int): Flow<RequestResult<Boolean>> {
        return movieRepository.checkMovieDetail(movieId)
    }
}