package com.example.mvvmexample1.presentation.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmexample1.data.util.NetworkManager
import com.example.mvvmexample1.domain.usecase.GetSearchMoviesUseCase

class MainViewModelFactory(
    private val app : Application,
    private val networkManager: NetworkManager,
    private val searchMoviesUseCase: GetSearchMoviesUseCase
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            app,
            networkManager,
            searchMoviesUseCase
        ) as T
    }
}