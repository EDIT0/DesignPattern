package com.example.mvvmarchitecturestudy2.presentation.di

import com.example.mvvmarchitecturestudy2.domain.repository.MovieRepository
import com.example.mvvmarchitecturestudy2.domain.usecase.GetMovieDetailUseCase
import com.example.mvvmarchitecturestudy2.domain.usecase.GetMovieReviewUseCase
import com.example.mvvmarchitecturestudy2.domain.usecase.GetPopularMoviesUseCase
import com.example.mvvmarchitecturestudy2.domain.usecase.GetSearchMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    @Singleton
    fun provideGetPopularMoviesUseCase(
        movieRepository: MovieRepository
    ): GetPopularMoviesUseCase {
        return GetPopularMoviesUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun provideGetSearchMoviesUseCase(
        movieRepository: MovieRepository
    ): GetSearchMoviesUseCase {
        return GetSearchMoviesUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun provideGetMovieDetailUseCase(
        movieRepository: MovieRepository
    ): GetMovieDetailUseCase {
        return GetMovieDetailUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun provideGetMovieReviewUseCase(
        movieRepository: MovieRepository
    ): GetMovieReviewUseCase {
        return GetMovieReviewUseCase(movieRepository)
    }
}