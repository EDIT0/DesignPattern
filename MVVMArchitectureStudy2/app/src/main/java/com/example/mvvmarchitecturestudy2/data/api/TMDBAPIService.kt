package com.example.mvvmarchitecturestudy2.data.api

import com.example.mvvmarchitecturestudy2.data.model.MovieDetailModel
import com.example.mvvmarchitecturestudy2.data.model.MovieModel
import com.example.mvvmarchitecturestudy2.data.model.MovieReviewModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBAPIService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") api_key : String,
        @Query("language") language : String,
        @Query("page") page : Int
    ) : Response<MovieModel>

    @GET("search/movie")
    suspend fun getSearchMovies(
        @Query("api_key") api_key : String,
        @Query("query") query: String,
        @Query("language") language : String,
        @Query("page") page : Int,
        @Query("include_adult") include_adult : Boolean = false
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