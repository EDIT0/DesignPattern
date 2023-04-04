package com.example.mvvmarchitecturestudy.domain.usecase

import com.example.mvvmarchitecturestudy.data.model.MovieModel
import com.example.mvvmarchitecturestudy.domain.repository.MoviesRepository
import retrofit2.Response

class GetSearchMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun execute(query : String, language : String, page : Int) : Response<MovieModel> {
        return moviesRepository.getSearchMovies(query, language, page)
    }
}