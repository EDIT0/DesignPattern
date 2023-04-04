package com.example.mvpexample1.model.repository.local

import com.example.mvpexample1.model.data.MovieModel
import com.example.mvpexample1.model.db.MovieDao
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