package com.example.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.util.NetworkManager
import com.example.domain.usecase.*

class MainViewModelFactory(
    val app : Application,
    val networkManager: NetworkManager,
    val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    val getSearchMoviesUseCase: GetSearchMoviesUseCase,
    val getSavedMoviesUseCase: GetSavedMoviesUseCase,
    val deleteSavedMovieUseCase: DeleteSavedMovieUseCase,
    val saveMovieUseCase: SaveMovieUseCase,
    val getSearchSavedMoviesUseCase : GetSearchSavedMoviesUseCase
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            app,
            networkManager,
            getPopularMoviesUseCase,
            getSearchMoviesUseCase,
            getSavedMoviesUseCase,
            deleteSavedMovieUseCase,
            saveMovieUseCase,
            getSearchSavedMoviesUseCase
        ) as T
    }
}