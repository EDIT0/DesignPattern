package com.my.mvistudymultimodule.core.di

import com.my.mvistudymultimodule.domain.repository.MovieRepository
import com.my.mvistudymultimodule.domain.usecase.CheckMovieDetailUseCase
import com.my.mvistudymultimodule.domain.usecase.DeleteMovieDetailUseCase
import com.my.mvistudymultimodule.domain.usecase.GetMovieDetailUseCase
import com.my.mvistudymultimodule.domain.usecase.GetPopularMovieUseCase
import com.my.mvistudymultimodule.domain.usecase.GetSearchMovieUseCase
import com.my.mvistudymultimodule.domain.usecase.SaveMovieDetailUseCase
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

    @Provides
    @Singleton
    fun provideGetMovieDetailUseCase(
        movieRepository: MovieRepository
    ): GetMovieDetailUseCase {
        return GetMovieDetailUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun provideSaveMovieDetailUseCase(
        movieRepository: MovieRepository
    ): SaveMovieDetailUseCase {
        return SaveMovieDetailUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteMovieDetailUseCase(
        movieRepository: MovieRepository
    ): DeleteMovieDetailUseCase {
        return DeleteMovieDetailUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun provideCheckMovieDetailUseCase(
        movieRepository: MovieRepository
    ): CheckMovieDetailUseCase {
        return CheckMovieDetailUseCase(movieRepository)
    }

//    @Provides
//    @Singleton
//    fun provideGetMovieReviewUseCase(
//        movieRepository: MovieRepository
//    ): GetMovieReviewUseCase {
//        return GetMovieReviewUseCase(movieRepository)
//    }
}