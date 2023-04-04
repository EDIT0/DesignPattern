package com.example.mvvmarchitecturestudy.presentation.di

import android.app.Application
import com.example.mvvmarchitecturestudy.data.util.NetworkManager
import com.example.mvvmarchitecturestudy.domain.usecase.*
import com.example.mvvmarchitecturestudy.presentation.viewmodel.MainViewModelFactory
import com.example.mvvmarchitecturestudy.presentation.viewmodel.MovieInfoViewModelFactory
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