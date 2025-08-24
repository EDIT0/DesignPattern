package com.my.mvistudymultimodule.feature.compose.searchmovie.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.my.mvistudymultimodule.core.base.BaseAndroidViewModel
import com.my.mvistudymultimodule.core.util.LogUtil
import com.my.mvistudymultimodule.domain.usecase.GetSearchMovieUseCase
import com.my.mvistudymultimodule.core.base.R
import com.my.mvistudymultimodule.feature.compose.searchmovie.event.ComposeSearchMovieViewModelEvent
import com.my.mvistudymultimodule.feature.compose.searchmovie.event.SearchStateUiEvent
import com.my.mvistudymultimodule.feature.compose.searchmovie.state.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComposeSearchMovieViewModel @Inject constructor(
    app: Application,
    private val getSearchMovieUseCase: GetSearchMovieUseCase
): BaseAndroidViewModel(app) {

    private val scope = viewModelScope
    private var scopeJob: Job? = null

    private var debounceJob: Job? = null

    private val language = "ko-KR"

    private val _sideEffectEvent = Channel<SideEffectEvent>()
    val sideEffectEvent = _sideEffectEvent.receiveAsFlow()

    private val _searchStateUiState = MutableStateFlow<SearchUiState>(SearchUiState())
    val searchStateUiState = _searchStateUiState.asStateFlow()

    /**
     * Ui update
     *
     * @param event
     */
    private fun eventSearchState(event: SearchStateUiEvent) {
        _searchStateUiState.update { state ->
            reducerSearchState(state, event)
        }
    }

    private fun reducerSearchState(state: SearchUiState, event: SearchStateUiEvent): SearchUiState {
        return when (event) {
            is SearchStateUiEvent.UpdateSearchKeyword -> {
                state.copy(searchKeyword = event.searchKeyword)
            }
            is SearchStateUiEvent.UpdateSearchMovieList -> {
                state.copy(searchMovieList = MutableStateFlow(value = event.searchMovieList!!))
            }
        }
    }

    fun handleViewModelEvent(composeSearchMovieViewModelEvent: ComposeSearchMovieViewModelEvent) {
        when(composeSearchMovieViewModelEvent) {
            is ComposeSearchMovieViewModelEvent.GetSearchMovie -> {

            }
            is ComposeSearchMovieViewModelEvent.SaveCurrentSearchKeyword -> {
                eventSearchState(SearchStateUiEvent.UpdateSearchKeyword(searchKeyword = composeSearchMovieViewModelEvent.searchKeyword))

                debounceJob?.cancel()
                debounceJob = scope.launch {
                    if(composeSearchMovieViewModelEvent.isDirectSearch) {

                    } else {
                        delay(2000L)
                    }

                    if(composeSearchMovieViewModelEvent.searchKeyword.isNotEmpty() && composeSearchMovieViewModelEvent.searchKeyword.length >= 2) {
                        scopeJob?.cancel()
                        scopeJob = scope.launch {
                            getSearchMovieUseCase.invokePaging(
                                query = composeSearchMovieViewModelEvent.searchKeyword,
                                language = language
                            ).map { it ->
                                it.map { model ->
                                    LogUtil.d_dev("페이징 데이터:  ${model}")
                                    model
                                }
                            }
                                .cachedIn(scope)
                                .collect {
                                    eventSearchState(SearchStateUiEvent.UpdateSearchMovieList(searchMovieList = it))
                                }
                        }
                    } else if(composeSearchMovieViewModelEvent.searchKeyword.isNotEmpty() && composeSearchMovieViewModelEvent.searchKeyword.length < 2) {
                        _sideEffectEvent.send(SideEffectEvent.Toast(app.getString(R.string.search_movie_search_text_length_warning)))
                    } else {
//                        _searchStateUiEvent.value = SearchStateUiEvent.UpdateSearchMovieList(searchMovieList = PagingData.empty())
                    }
                }

            }
        }
    }

    sealed interface SideEffectEvent {
        data class Toast(val message: String) : SideEffectEvent
    }

    override fun onCleared() {
        super.onCleared()

        _sideEffectEvent.close()
    }
}