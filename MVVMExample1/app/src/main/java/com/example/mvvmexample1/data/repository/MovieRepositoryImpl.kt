package com.example.mvvmexample1.data.repository

import com.example.mvvmexample1.data.model.MovieModel
import com.example.mvvmexample1.data.repository.local.MovieLocalDataSource
import com.example.mvvmexample1.data.repository.remote.MovieRemoteDataSource
import com.example.mvvmexample1.data.util.ERROR
import com.example.mvvmexample1.data.util.NO_DATA
import com.example.mvvmexample1.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieRepositoryImpl(
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val movieLocalDataSource: MovieLocalDataSource
) : MovieRepository {

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