package com.example.mvvmexample1.presentation.di

import com.example.mvvmexample1.data.repository.MovieRepositoryImpl
import com.example.mvvmexample1.data.repository.local.MovieLocalDataSource
import com.example.mvvmexample1.data.repository.remote.MovieRemoteDataSource
import com.example.mvvmexample1.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideMovieRepository(
        movieRemoteDataSource: MovieRemoteDataSource,
        movieLocalDataSource: MovieLocalDataSource
    ) : MovieRepository {
        return MovieRepositoryImpl(
            movieRemoteDataSource,
            movieLocalDataSource
        )
    }
}