package com.example.mvvmexample1.presentation.di

import android.app.Application
import com.example.mvvmexample1.data.util.NetworkManager
import com.example.mvvmexample1.domain.usecase.GetSearchMoviesUseCase
import com.example.mvvmexample1.presentation.ui.viewmodel.MainViewModelFactory
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
        searchMoviesUseCase: GetSearchMoviesUseCase
        ): MainViewModelFactory {
        return MainViewModelFactory(
            application,
            networkManager,
            searchMoviesUseCase
        )
    }
}