package com.example.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.usecase.SaveMovieUseCase

class MovieInfoViewModelFactory(
    val app : Application,
    val saveMovieUseCase: SaveMovieUseCase,
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieInfoViewModel(
            app,
            saveMovieUseCase
        ) as T
    }
}