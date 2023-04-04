package com.example.mvvmexample1.presentation.di

import com.example.mvvmexample1.domain.repository.MovieRepository
import com.example.mvvmexample1.domain.usecase.DeleteMovieUseCase
import com.example.mvvmexample1.domain.usecase.GetAllSavedMoviesUseCase
import com.example.mvvmexample1.domain.usecase.GetSearchMoviesUseCase
import com.example.mvvmexample1.domain.usecase.InsertMovieUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetSearchMoviesUseCase(
        movieRepository: MovieRepository
    ): GetSearchMoviesUseCase {
        return GetSearchMoviesUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun provideInsertMovieUseCase(
        movieRepository: MovieRepository
    ): InsertMovieUseCase {
        return InsertMovieUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun provideGetAllSavedMoviesUseCase(
        movieRepository: MovieRepository
    ): GetAllSavedMoviesUseCase {
        return GetAllSavedMoviesUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteMovieUseCase(
        movieRepository: MovieRepository
    ): DeleteMovieUseCase {
        return DeleteMovieUseCase(movieRepository)
    }
}