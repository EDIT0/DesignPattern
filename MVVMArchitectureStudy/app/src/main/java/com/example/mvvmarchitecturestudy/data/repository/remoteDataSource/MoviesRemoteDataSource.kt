package com.example.mvvmarchitecturestudy.data.repository.remoteDataSource

import com.example.mvvmarchitecturestudy.data.model.MovieModel
import retrofit2.Response

interface MoviesRemoteDataSource {

    suspend fun getPopularMovies(language : String, page : Int) :Response<MovieModel>
    suspend fun getSearchMovies(query : String, language: String, page: Int) : Response<MovieModel>


}