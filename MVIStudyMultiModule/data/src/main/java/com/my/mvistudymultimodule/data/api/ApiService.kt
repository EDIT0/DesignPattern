package com.my.mvistudymultimodule.data.api

import com.my.mvistudymultimodule.core.model.MovieDetailModel
import com.my.mvistudymultimodule.core.model.MovieModel
import com.my.mvistudymultimodule.core.model.MovieReviewModel
import com.my.mvistudymultimodule.data.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("discover/movie")
    suspend fun getPopularMovie(
        @Query("api_key") api_key: String = BuildConfig.API_KEY,
        @Query("language") language: String = "ko-KR",
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("include_adult") include_adult: Boolean = false,
        @Query("include_video") include_video: Boolean = true
    ) : Response<MovieModel>

    @GET("search/movie")
    suspend fun getSearchMovie(
        @Query("api_key") api_key: String = BuildConfig.API_KEY,
        @Query("query") query: String,
        @Query("language") language: String = "ko-KR",
        @Query("page") page: Int,
        @Query("include_adult") include_adult: Boolean = false
    ) : Response<MovieModel>

    @GET("movie/{movieId}")
    suspend fun getMovieDetail(
        @Path("movieId") movieId: Int,
        @Query("api_key") api_key : String,
        @Query("language") language : String
    ) : Response<MovieDetailModel>

    @GET("movie/{movieId}/reviews")
    suspend fun getMovieReview(
        @Path("movieId") movieId: Int,
        @Query("api_key") api_key : String,
        @Query("language") language : String,
        @Query("page") page: Int
    ) : Response<MovieReviewModel>
}