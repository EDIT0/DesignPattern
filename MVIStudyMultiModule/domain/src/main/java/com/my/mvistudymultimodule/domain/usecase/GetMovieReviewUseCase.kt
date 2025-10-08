package com.my.mvistudymultimodule.domain.usecase

import androidx.paging.PagingData
import com.my.mvistudymultimodule.core.model.MovieReviewModel
import com.my.mvistudymultimodule.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieReviewUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend fun invoke(movieId: Int, language: String): Flow<PagingData<MovieReviewModel.Result>> {
        return repository.getMovieReviewPaging(language, movieId)
    }
}