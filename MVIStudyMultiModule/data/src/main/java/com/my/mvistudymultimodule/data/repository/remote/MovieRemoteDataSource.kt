package com.my.mvistudymultimodule.data.repository.remote

import com.my.mvistudymultimodule.core.model.MovieModel
import retrofit2.Response

interface MovieRemoteDataSource {
    suspend fun getPopularMovie(language : String, page : Int) : Response<MovieModel>
    suspend fun getSearchMovie(query : String, language: String, page: Int) : Response<MovieModel>
}