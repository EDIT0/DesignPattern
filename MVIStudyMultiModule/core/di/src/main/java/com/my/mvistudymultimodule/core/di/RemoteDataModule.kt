package com.my.mvistudymultimodule.core.di

import com.my.mvistudymultimodule.data.api.ApiService
import com.my.mvistudymultimodule.data.repository.remote.MovieRemoteDataSource
import com.my.mvistudymultimodule.data.repository.remote.MovieRemoteDataSourceImpl
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
    fun provideMovieRemoteDataSource(
        apiService: ApiService
    ) : MovieRemoteDataSource {
        return MovieRemoteDataSourceImpl(apiService)
    }

}