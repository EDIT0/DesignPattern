package com.my.mvistudymultimodule.domain.repository

import com.my.mvistudymultimodule.core.base.RequestResult
import com.my.mvistudymultimodule.core.model.MovieModel
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovies(language : String, page : Int) : Flow<RequestResult<MovieModel>>
    suspend fun getSearchMovies(query : String, language: String, page: Int) : Flow<RequestResult<MovieModel>>
}