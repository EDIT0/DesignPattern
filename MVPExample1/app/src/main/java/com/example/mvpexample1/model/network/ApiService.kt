package com.example.mvpexample1.model.network

import com.example.mvpexample1.model.data.MovieModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search/movie")
    suspend fun getSearchMovies(
        @Query("api_key") api_key : String,
        @Query("query") query: String,
        @Query("language") language : String,
        @Query("page") page : Int,
        @Query("include_adult") include_adult : Boolean = false
    ) : Response<MovieModel>
}