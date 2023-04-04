package com.example.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.MovieModelResult
import com.example.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow

class GetSearchSavedMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {
    fun execute(keyword : String) : LiveData<List<MovieModelResult>> {
        return moviesRepository.getSearchSavedMovies(keyword)
    }

    fun execute_using_stateflow(keyword: String) : Flow<List<MovieModelResult>> {
        return moviesRepository.getSearchSavedMovies_using_stateflow(keyword)
    }
}