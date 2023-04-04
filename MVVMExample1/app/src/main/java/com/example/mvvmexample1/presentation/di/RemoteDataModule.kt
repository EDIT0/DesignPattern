package com.example.mvvmexample1.presentation.di

import com.example.mvvmexample1.data.network.ApiService
import com.example.mvvmexample1.data.repository.remote.MovieRemoteDataSource
import com.example.mvvmexample1.data.repository.remote.MovieRemoteDataSourceImpl
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