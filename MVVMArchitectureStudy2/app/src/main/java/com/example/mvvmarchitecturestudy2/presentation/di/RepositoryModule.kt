package com.example.mvvmarchitecturestudy2.presentation.di

import com.example.mvvmarchitecturestudy2.data.repository.MovieRepositoryImpl
import com.example.mvvmarchitecturestudy2.data.repository.remote.MovieRemoteDataSource
import com.example.mvvmarchitecturestudy2.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        movieRemoteDataSource: MovieRemoteDataSource
    ) : MovieRepository {
        return MovieRepositoryImpl(
            movieRemoteDataSource
        )
    }

}