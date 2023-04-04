package com.example.presentation.di

import android.app.Application
import com.example.data.util.NetworkManager
import com.example.domain.usecase.*
import com.example.presentation.viewmodel.MainViewModelFactory
import com.example.presentation.viewmodel.MovieInfoViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object FactoryModule {

    @Provides
    @Singleton
    fun provideMainViewModelFactory(
        application: Application,
        networkManager: NetworkManager,
        getPopularMoviesUseCase: GetPopularMoviesUseCase,
        searchMoviesUseCase: GetSearchMoviesUseCase,
        getSavedMoviesUseCase: GetSavedMoviesUseCase,
        deleteSavedMovieUseCase: DeleteSavedMovieUseCase,
        saveMovieUseCase: SaveMovieUseCase,
        getSearchSavedMoviesUseCase : GetSearchSavedMoviesUseCase
    ) : MainViewModelFactory {
        return MainViewModelFactory(
            application, networkManager, getPopularMoviesUseCase, searchMoviesUseCase, getSavedMoviesUseCase, deleteSavedMovieUseCase, saveMovieUseCase, getSearchSavedMoviesUseCase
        )
    }

    @Provides
    @Singleton
    fun provideMovieInfoViewModelFactory(
        application: Application,
        saveMovieUseCase: SaveMovieUseCase
    ) : MovieInfoViewModelFactory {
        return MovieInfoViewModelFactory(
            application, saveMovieUseCase
        )
    }

}