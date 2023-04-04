package com.example.mvvmarchitecturestudy.domain.usecase

import com.example.mvvmarchitecturestudy.data.model.MovieModelResult
import com.example.mvvmarchitecturestudy.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

class GetSavedMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {
    fun execute() : Flow<List<MovieModelResult>>{
        return moviesRepository.getSavedMovies()
    }
}