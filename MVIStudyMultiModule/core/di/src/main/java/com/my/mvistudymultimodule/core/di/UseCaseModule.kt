package com.my.mvistudymultimodule.core.di

import com.my.mvistudymultimodule.domain.repository.MovieRepository
import com.my.mvistudymultimodule.domain.usecase.GetPopularMovieUseCase
import com.my.mvistudymultimodule.domain.usecase.GetSearchMovieUseCase
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
    fun provideGetPopularMovieUseCase(
        movieRepository: MovieRepository
    ): GetPopularMovieUseCase {
        return GetPopularMovieUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun provideGetSearchMovieUseCase(
        movieRepository: MovieRepository
    ): GetSearchMovieUseCase {
        return GetSearchMovieUseCase(movieRepository)
    }

//    @Provides
//    @Singleton
//    fun provideGetMovieDetailUseCase(
//        movieRepository: MovieRepository
//    ): GetMovieDetailUseCase {
//        return GetMovieDetailUseCase(movieRepository)
//    }

//    @Provides
//    @Singleton
//    fun provideGetMovieReviewUseCase(
//        movieRepository: MovieRepository
//    ): GetMovieReviewUseCase {
//        return GetMovieReviewUseCase(movieRepository)
//    }
}