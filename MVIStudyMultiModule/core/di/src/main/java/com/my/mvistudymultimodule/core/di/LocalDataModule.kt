package com.my.mvistudymultimodule.core.di


import com.my.mvistudymultimodule.core.database.dao.MovieDetailDao
import com.my.mvistudymultimodule.data.repository.local.MovieLocalDataSource
import com.my.mvistudymultimodule.data.repository.local.MovieLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Provides
    @Singleton
    fun provideMovieLocalDataSource(
        movieDetailDao: MovieDetailDao
    ) : MovieLocalDataSource {
        return MovieLocalDataSourceImpl(movieDetailDao)
    }
}