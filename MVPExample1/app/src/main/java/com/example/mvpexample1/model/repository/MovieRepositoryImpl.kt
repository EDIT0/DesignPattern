package com.example.mvpexample1.model.repository

import com.example.mvpexample1.model.data.MovieModel
import com.example.mvpexample1.model.repository.local.MovieLocalDataSource
import com.example.mvpexample1.model.repository.remote.MovieRemoteDataSource
import com.example.mvpexample1.model.util.ERROR
import com.example.mvpexample1.model.util.NO_DATA
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieRepositoryImpl(
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val movieLocalDataSource: MovieLocalDataSource
) : MovieRepository{

    override suspend fun getSearchMovies(
        apiKey: String,
        query: String,
        language: String,
        page: Int
    ): Flow<MovieModel> {
        return flow {
            val response = movieRemoteDataSource.getSearchMovies(apiKey, query, language, page)

            if(response.isSuccessful) {
                if(response.body()?.movieModelResults?.isEmpty() == true) {
                    throw Exception(NO_DATA)
                } else {
                    response.body()?.let {
                        emit(it)
                    }
                }
            } else {
                throw Exception(ERROR)
            }
        }
    }

    override suspend fun insertMovie(data: MovieModel.MovieModelResult) {
        movieLocalDataSource.insertMovie(data)
    }

    override suspend fun getAllSavedMovies(): Flow<List<MovieModel.MovieModelResult>> {
        return movieLocalDataSource.getAllSavedMovies()
    }

    override suspend fun deleteMovie(data: MovieModel.MovieModelResult) {
        movieLocalDataSource.deleteMovie(data)
    }
}