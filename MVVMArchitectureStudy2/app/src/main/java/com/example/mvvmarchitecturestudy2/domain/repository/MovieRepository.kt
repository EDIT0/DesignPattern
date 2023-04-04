package com.example.mvvmarchitecturestudy2.domain.repository

import com.example.mvvmarchitecturestudy2.data.model.MovieDetailModel
import com.example.mvvmarchitecturestudy2.data.model.MovieModel
import com.example.mvvmarchitecturestudy2.data.model.MovieReviewModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MovieRepository {

    suspend fun getPopularMovies(language : String, page : Int) : Flow<MovieModel>
    suspend fun getSearchMovies(query : String, language: String, page: Int) : Flow<MovieModel>
    suspend fun getMovieDetail(movieId: Int, language: String) : Flow<MovieDetailModel>
    suspend fun getMovieReview(movieId: Int, language: String, page: Int) : Flow<MovieReviewModel>
}