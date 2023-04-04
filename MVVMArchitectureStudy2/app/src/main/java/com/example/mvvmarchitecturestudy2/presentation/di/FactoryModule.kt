package com.example.mvvmarchitecturestudy2.presentation.di

import com.example.mvvmarchitecturestudy2.data.util.NetworkManager
import com.example.mvvmarchitecturestudy2.domain.usecase.GetMovieDetailUseCase
import com.example.mvvmarchitecturestudy2.domain.usecase.GetMovieReviewUseCase
import com.example.mvvmarchitecturestudy2.domain.usecase.GetPopularMoviesUseCase
import com.example.mvvmarchitecturestudy2.domain.usecase.GetSearchMoviesUseCase
import com.example.mvvmarchitecturestudy2.presentation.viewmodel.MainViewModel
import com.example.mvvmarchitecturestudy2.presentation.viewmodel.MovieInfoViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(ActivityComponent::class)
class FactoryModule {

    @Provides
    fun provideMainViewModel(
        networkManager: NetworkManager,
        getPopularMoviesUseCase: GetPopularMoviesUseCase,
        getSearchMoviesUseCase: GetSearchMoviesUseCase
    ) : MainViewModel {
        return MainViewModel(
            networkManager,
            getPopularMoviesUseCase,
            getSearchMoviesUseCase
        )
    }

    @Provides
    fun provideMovieInfoViewModel(
        networkManager: NetworkManager,
        getMovieDetailUseCase: GetMovieDetailUseCase,
        getMovieReviewUseCase: GetMovieReviewUseCase
    ) : MovieInfoViewModel {
        return MovieInfoViewModel(
            networkManager,
            getMovieDetailUseCase,
            getMovieReviewUseCase
        )
    }

}