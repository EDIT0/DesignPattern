package com.example.presentation.di

import com.example.data.api.TmdbAPIService
import com.example.data.repository.remoteDataSource.MoviesRemoteDataSource
import com.example.data.repository.remoteDataSourceImpl.MoviesRemoteDataSourceImpl
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
    fun provideMoviesRemoteDataSource(tmdbAPIService: TmdbAPIService) : MoviesRemoteDataSource {
        return MoviesRemoteDataSourceImpl(tmdbAPIService)
    }

}