package com.my.mvistudymultimodule.feature.compose.savedmovie.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.my.mvistudymultimodule.core.base.BaseAndroidViewModel
import com.my.mvistudymultimodule.core.model.MovieDetailModel
import com.my.mvistudymultimodule.core.util.LogUtil
import com.my.mvistudymultimodule.domain.usecase.GetSavedMovieUseCase
import com.my.mvistudymultimodule.feature.compose.savedmovie.event.ComposeSavedMovieViewModelEvent
import com.my.mvistudymultimodule.feature.compose.savedmovie.event.SavedMovieListPagingUiEvent
import com.my.mvistudymultimodule.feature.compose.savedmovie.state.SavedMovieListPagingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComposeSavedMovieViewModel @Inject constructor(
    app: Application,
    private val getSavedMovieUseCase: GetSavedMovieUseCase
): BaseAndroidViewModel(app){

    private val scope = viewModelScope
    private var scopeJob: Job? = null


    private val _savedMovieListPagingUiEvent: MutableStateFlow<SavedMovieListPagingUiEvent> = MutableStateFlow(SavedMovieListPagingUiEvent.Idle)
    val savedMovieListPagingUiState: StateFlow<SavedMovieListPagingUiState> = _savedMovieListPagingUiEvent.runningFold(
        SavedMovieListPagingUiState()
    ) { state, event ->
        when(event) {
            is SavedMovieListPagingUiEvent.Idle -> {
                state.copy(savedMovieList = MutableStateFlow(value = PagingData.empty()))
            }
            is SavedMovieListPagingUiEvent.UpdateSavedMovieList -> {
                state.copy(savedMovieList = MutableStateFlow(value = event.savedMovieList!!))
            }
        }
    }.stateIn(scope, SharingStarted.Eagerly, SavedMovieListPagingUiState())

    fun handleViewModelEvent(composeSavedMovieViewModelEvent: ComposeSavedMovieViewModelEvent) {
        when(composeSavedMovieViewModelEvent) {
            is ComposeSavedMovieViewModelEvent.GetSavedMovie -> {
                getSavedMoviePaging()
            }
        }
    }

    /**
     * 저장된 영화 데이터 요청 (페이징)
     */
    private fun getSavedMoviePaging() {
        scopeJob?.cancel()
        scopeJob = scope.launch {
            getSavedMovieUseCase.invokePaging()
                .map { it ->
                    it.map { model ->
                        LogUtil.d_dev("페이징 데이터:  ${model}")
                        model
                    }
                }
                .cachedIn(scope)
                .collect {
                    _savedMovieListPagingUiEvent.value = SavedMovieListPagingUiEvent.UpdateSavedMovieList(savedMovieList = it)
                }
        }
    }
}