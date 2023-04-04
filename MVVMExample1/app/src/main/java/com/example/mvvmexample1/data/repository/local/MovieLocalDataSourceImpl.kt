package com.example.mvvmexample1.data.repository.local

import com.example.mvvmexample1.data.model.MovieModel
import com.example.mvvmexample1.data.db.MovieDao
import kotlinx.coroutines.flow.Flow

class MovieLocalDataSourceImpl(
    private val movieDao: MovieDao
) : MovieLocalDataSource {
    override suspend fun insertMovie(data: MovieModel.MovieModelResult) {
        movieDao.insertMovie(data)
    }

    override suspend fun getAllSavedMovies(): Flow<List<MovieModel.MovieModelResult>> {
        return movieDao.getAllSavedMovies()
    }

    override suspend fun deleteMovie(data: MovieModel.MovieModelResult) {
        movieDao.deleteSavedMovies(data)
    }
}