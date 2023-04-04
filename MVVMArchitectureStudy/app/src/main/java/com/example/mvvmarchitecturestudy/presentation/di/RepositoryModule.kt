package com.example.mvvmarchitecturestudy.presentation.di

import com.example.mvvmarchitecturestudy.data.repository.MoviesRepositoryImpl
import com.example.mvvmarchitecturestudy.data.repository.localDataSource.MovieLocalDataSource
import com.example.mvvmarchitecturestudy.data.repository.remoteDataSource.MoviesRemoteDataSource
import com.example.mvvmarchitecturestudy.domain.repository.MoviesRepository
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
    fun provideMoviesRepository(
        moviesRemoteDataSource: MoviesRemoteDataSource,
        movieLocalDataSource: MovieLocalDataSource
    ) : MoviesRepository {
        return MoviesRepositoryImpl(moviesRemoteDataSource, movieLocalDataSource)
    }

}