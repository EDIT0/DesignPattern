package com.my.mvistudymultimodule.core.di

import com.my.mvistudymultimodule.data.repository.MovieRepositoryImpl
import com.my.mvistudymultimodule.data.repository.remote.MovieRemoteDataSource
import com.my.mvistudymultimodule.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

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