package com.my.mvistudymultimodule.domain.usecase

import androidx.paging.PagingData
import com.my.mvistudymultimodule.core.model.MovieDetailModel
import com.my.mvistudymultimodule.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    fun invokePaging(): Flow<PagingData<MovieDetailModel>> {
        return movieRepository.getSavedMoviePaging()
    }
}