package com.example.mvvmarchitecturestudy2.presentation.di

import com.example.mvvmarchitecturestudy2.presentation.adapter.MovieAdapter
import com.example.mvvmarchitecturestudy2.presentation.adapter.MovieReviewAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdapterModule {

    @Provides
    @Singleton
    fun provideMovieAdapter() : MovieAdapter {
        return MovieAdapter()
    }

    @Provides
    @Singleton
    fun provideMovieReviewAdapter() : MovieReviewAdapter {
        return MovieReviewAdapter()
    }
}