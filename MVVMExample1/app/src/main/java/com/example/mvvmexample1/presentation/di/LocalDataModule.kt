package com.example.mvvmexample1.presentation.di

import com.example.mvvmexample1.data.db.MovieDao
import com.example.mvvmexample1.data.repository.local.MovieLocalDataSource
import com.example.mvvmexample1.data.repository.local.MovieLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {
    @Provides
    @Singleton
    fun provideMovieLocalDataSource(movieDao: MovieDao) : MovieLocalDataSource {
        return MovieLocalDataSourceImpl(movieDao)
    }
}