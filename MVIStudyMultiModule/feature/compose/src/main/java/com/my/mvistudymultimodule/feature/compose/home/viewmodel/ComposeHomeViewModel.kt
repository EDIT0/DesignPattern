package com.my.mvistudymultimodule.feature.compose.home.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.my.mvistudymultimodule.core.base.BaseAndroidViewModel
import com.my.mvistudymultimodule.domain.usecase.GetPopularMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class ComposeHomeViewModel @Inject constructor(
    app: Application,
    private val getPopularMovieUseCase: GetPopularMovieUseCase
): BaseAndroidViewModel(app){

    private val scope = viewModelScope
    private var scopeJob: Job? = null

}