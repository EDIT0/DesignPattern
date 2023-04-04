package com.example.mvpexample1.view.di

import com.example.mvpexample1.model.db.MovieDao
import com.example.mvpexample1.model.network.ApiService
import com.example.mvpexample1.model.repository.local.MovieLocalDataSource
import com.example.mvpexample1.model.repository.local.MovieLocalDataSourceImpl
import com.example.mvpexample1.model.repository.remote.MovieRemoteDataSource
import com.example.mvpexample1.model.repository.remote.MovieRemoteDataSourceImpl
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