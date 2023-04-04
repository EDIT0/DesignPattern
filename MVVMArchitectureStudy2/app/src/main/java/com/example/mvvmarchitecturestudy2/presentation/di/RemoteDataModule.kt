package com.example.mvvmarchitecturestudy2.presentation.di

import com.example.mvvmarchitecturestudy2.data.api.TMDBAPIService
import com.example.mvvmarchitecturestudy2.data.repository.remote.MovieRemoteDataSource
import com.example.mvvmarchitecturestudy2.data.repository.remote.MovieRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataModule {

    @Provides
    @Singleton
    fun provideMovieRemoteDataSource(tmdbapiService: TMDBAPIService) : MovieRemoteDataSource {
        return MovieRemoteDataSourceImpl(tmdbapiService)
    }

}