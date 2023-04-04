package com.example.presentation.di

import com.example.data.db.MovieDAO
import com.example.data.repository.localDataSource.MovieLocalDataSource
import com.example.data.repository.localDataSourceImpl.MovieLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Singleton
    @Provides
    fun provideMovieLocalDataSource(movieDAO: MovieDAO) : MovieLocalDataSource {
        return MovieLocalDataSourceImpl(movieDAO)
    }
}