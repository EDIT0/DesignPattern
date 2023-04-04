package com.example.mvpexample1.view.di

import com.example.mvpexample1.model.network.ApiService
import com.example.mvpexample1.model.repository.remote.MovieRemoteDataSource
import com.example.mvpexample1.model.repository.remote.MovieRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataModule {

    @Provides
    @Singleton
    fun provideMovieRemoteDataSource(apiService: ApiService) : MovieRemoteDataSource {
        return MovieRemoteDataSourceImpl(apiService)
    }

}