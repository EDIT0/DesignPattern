package com.example.mvvmarchitecturestudy2.data.repository.remote

import com.example.mvvmarchitecturestudy2.data.model.MovieDetailModel
import com.example.mvvmarchitecturestudy2.data.model.MovieModel
import com.example.mvvmarchitecturestudy2.data.model.MovieReviewModel
import retrofit2.Response

interface MovieRemoteDataSource {

    suspend fun getPopularMovies(language : String, page : Int) : Response<MovieModel>
    suspend fun getSearchMovies(query : String, language: String, page: Int) : Response<MovieModel>
    suspend fun getMovieDetail(movieId: Int, language: String) : Response<MovieDetailModel>
    suspend fun getMovieReview(movieId: Int, language: String, page: Int) : Response<MovieReviewModel>
}