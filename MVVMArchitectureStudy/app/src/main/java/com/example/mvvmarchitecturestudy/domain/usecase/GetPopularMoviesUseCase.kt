package com.example.mvvmarchitecturestudy.domain.usecase

import com.example.mvvmarchitecturestudy.data.model.MovieModel
import com.example.mvvmarchitecturestudy.domain.repository.MoviesRepository
import retrofit2.Response

class GetPopularMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun execute(language : String, page : Int) : Response<MovieModel> {
        return moviesRepository.getPopularMovies(language, page)
    }
}