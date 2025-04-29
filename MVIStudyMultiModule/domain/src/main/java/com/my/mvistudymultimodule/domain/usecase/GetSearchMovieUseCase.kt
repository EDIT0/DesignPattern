package com.my.mvistudymultimodule.domain.usecase

import androidx.paging.PagingData
import com.my.mvistudymultimodule.core.base.RequestResult
import com.my.mvistudymultimodule.core.model.MovieModel
import com.my.mvistudymultimodule.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend fun invoke(query: String, language: String, page: Int): Flow<RequestResult<MovieModel>> {
        return movieRepository.getSearchMovie(query, language, page)
    }

    suspend fun invokePaging(query: String, language: String): Flow<PagingData<MovieModel.MovieModelResult>> {
        return movieRepository.getSearchMoviePaging(query, language)
    }
}