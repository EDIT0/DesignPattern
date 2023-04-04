package com.example.data.repository.localDataSourceImpl

import androidx.lifecycle.LiveData
import com.example.data.db.MovieDAO
import com.example.domain.model.MovieModelResult
import com.example.data.repository.localDataSource.MovieLocalDataSource
import kotlinx.coroutines.flow.Flow

class MovieLocalDataSourceImpl(
    private val movieDAO: MovieDAO
) : MovieLocalDataSource {
    override suspend fun saveMovie(movieModelResult: MovieModelResult){
        movieDAO.insertMovie(movieModelResult)
    }

    override fun getSavedMovies(): Flow<List<MovieModelResult>> {
        return movieDAO.getAllSavedMovies()
    }

    override suspend fun deleteSavedMovie(movieModelResult: MovieModelResult) {
        movieDAO.deleteSavedMovies(movieModelResult)
    }

    override fun getSearchSavedMovies(keyword: String): LiveData<List<MovieModelResult>> {
        return movieDAO.getSearchSavedMovies(keyword)
    }

    override fun getSearchSavedMovies_using_stateflow(keyword: String): Flow<List<MovieModelResult>> {
        return movieDAO.getSearchSavedMovies_using_stateflow(keyword)
    }
}