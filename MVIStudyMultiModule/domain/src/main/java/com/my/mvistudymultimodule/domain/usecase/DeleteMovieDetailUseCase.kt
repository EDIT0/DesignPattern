package com.my.mvistudymultimodule.domain.usecase

import com.my.mvistudymultimodule.core.base.RequestResult
import com.my.mvistudymultimodule.core.model.MovieDetailModel
import com.my.mvistudymultimodule.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteMovieDetailUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend fun invoke(movieDetail: MovieDetailModel): Flow<RequestResult<Boolean>> {
        return movieRepository.deleteMovieDetail(movieDetail)
    }
}