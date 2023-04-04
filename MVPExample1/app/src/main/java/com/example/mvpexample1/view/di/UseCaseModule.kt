package com.example.mvpexample1.view.di

import com.example.mvpexample1.model.repository.MovieRepository
import com.example.mvpexample1.model.usecase.DeleteMovieUseCase
import com.example.mvpexample1.model.usecase.GetAllSavedMoviesUseCase
import com.example.mvpexample1.model.usecase.GetSearchMoviesUseCase
import com.example.mvpexample1.model.usecase.InsertMovieUseCase
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