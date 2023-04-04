package com.example.mvpexample1.view.di

import com.example.mvpexample1.model.repository.MovieRepository
import com.example.mvpexample1.model.repository.MovieRepositoryImpl
import com.example.mvpexample1.model.repository.local.MovieLocalDataSource
import com.example.mvpexample1.model.repository.remote.MovieRemoteDataSource
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