package com.example.domain.usecase

import com.example.domain.model.MovieModel
import com.example.domain.repository.MoviesRepository
import retrofit2.Response

class GetSearchMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun execute(query : String, language : String, page : Int) : Response<MovieModel> {
        return moviesRepository.getSearchMovies(query, language, page)
    }
}