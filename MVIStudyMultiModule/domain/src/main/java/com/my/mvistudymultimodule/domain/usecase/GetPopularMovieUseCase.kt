package com.my.mvistudymultimodule.domain.usecase

import com.my.mvistudymultimodule.core.base.RequestResult
import com.my.mvistudymultimodule.core.model.MovieModel
import com.my.mvistudymultimodule.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPopularMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend fun invoke(language: String, page: Int): Flow<RequestResult<MovieModel>> {
        return movieRepository.getPopularMovies(language, page)
    }
}