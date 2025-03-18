package com.my.mvistudymultimodule.data.api

import com.my.mvistudymultimodule.core.model.MovieModel
import com.my.mvistudymultimodule.data.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") api_key: String = BuildConfig.API_KEY,
        @Query("language") language: String = "ko-KR",
        @Query("page") page: Int
    ) : Response<MovieModel>

    @GET("search/movie")
    suspend fun getSearchMovies(
        @Query("api_key") api_key: String = BuildConfig.API_KEY,
        @Query("query") query: String,
        @Query("language") language: String = "ko-KR",
        @Query("page") page: Int,
        @Query("include_adult") include_adult: Boolean = false
    ) : Response<MovieModel>
}