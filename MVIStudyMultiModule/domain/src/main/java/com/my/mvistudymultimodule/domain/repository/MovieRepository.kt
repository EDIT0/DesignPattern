package com.my.mvistudymultimodule.domain.repository

import com.my.mvistudymultimodule.core.base.RequestResult
import com.my.mvistudymultimodule.core.model.MovieModel
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovie(language : String, page : Int) : Flow<RequestResult<MovieModel>>
    suspend fun getSearchMovie(query : String, language: String, page: Int) : Flow<RequestResult<MovieModel>>
}