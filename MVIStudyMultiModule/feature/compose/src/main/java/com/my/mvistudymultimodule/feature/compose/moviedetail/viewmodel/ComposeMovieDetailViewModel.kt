package com.my.mvistudymultimodule.feature.compose.moviedetail.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.my.mvistudymultimodule.core.base.BaseAndroidViewModel
import com.my.mvistudymultimodule.core.base.RequestResult
import com.my.mvistudymultimodule.core.model.MovieDetailModel
import com.my.mvistudymultimodule.core.model.MovieModel
import com.my.mvistudymultimodule.core.util.LogUtil
import com.my.mvistudymultimodule.domain.usecase.CheckMovieDetailUseCase
import com.my.mvistudymultimodule.domain.usecase.DeleteMovieDetailUseCase
import com.my.mvistudymultimodule.domain.usecase.GetMovieDetailUseCase
import com.my.mvistudymultimodule.domain.usecase.SaveMovieDetailUseCase
import com.my.mvistudymultimodule.feature.compose.moviedetail.event.ComposeMovieDetailViewModelEvent
import com.my.mvistudymultimodule.feature.compose.moviedetail.event.MovieDetailUiEvent
import com.my.mvistudymultimodule.feature.compose.moviedetail.state.MovieDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComposeMovieDetailViewModel @Inject constructor(
    app: Application,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val saveMovieDetailUseCase: SaveMovieDetailUseCase,
    private val deleteMovieDetailUseCase: DeleteMovieDetailUseCase,
    private val checkMovieDetailUseCase: CheckMovieDetailUseCase
): BaseAndroidViewModel(app) {

    private val scope = viewModelScope
    private var scopeJob: Job? = null

    private val language = "ko-KR"
    private var movieId: Int = -1
    private var movieInfo: MovieModel.MovieModelResult? = null

    private val _movieDetailUiEvent = Channel<MovieDetailUiEvent>(capacity = Channel.UNLIMITED)
    val movieDetailUiState = _movieDetailUiEvent.receiveAsFlow()
        .runningFold(
            initial = MovieDetailUiState(),
            operation = { state, event ->
                when(event) {
                    is MovieDetailUiEvent.UpdateLoading -> {
                        state.copy(isLoading = event.isLoading)
                    }
                    is MovieDetailUiEvent.UpdateMovieDetail -> {
                        state.copy(movieDetail = event.movieDetail)
                    }
                    is MovieDetailUiEvent.UpdateSaveState -> {
                        state.copy(isSaveState = event.isSaveState)
                    }
                }
            }
        )
        .stateIn(scope, SharingStarted.Eagerly, MovieDetailUiState())

//    private val _movieDetailUiState = MutableStateFlow(MovieDetailUiState())
//    val movieDetailUiState = _movieDetailUiState.asStateFlow()
    
    private val _sideEffectEvent = Channel<SideEffectEvent>()
    val sideEffectEvent = _sideEffectEvent.receiveAsFlow()

//    /**
//     * Ui update
//     *
//     * @param event
//     */
//    private fun eventMovieDetail(event: MovieDetailUiEvent) {
//        _movieDetailUiState.update { state ->
//            reducerMovieDetail(
//                state = state,
//                event = event
//            )
//        }
//    }
//
//    private fun reducerMovieDetail(state: MovieDetailUiState, event: MovieDetailUiEvent): MovieDetailUiState {
//        return when(event) {
//            is MovieDetailUiEvent.UpdateLoading -> {
//                state.copy(isLoading = event.isLoading)
//            }
//            is MovieDetailUiEvent.UpdateMovieDetail -> {
//                state.copy(movieDetail = event.movieDetail)
//            }
//            is MovieDetailUiEvent.UpdateSaveState -> {
//                state.copy(isSaveState = event.isSaveState)
//            }
//        }
//    }

    fun handleViewModelEvent(composeMovieDetailViewModelEvent: ComposeMovieDetailViewModelEvent) {
        when(composeMovieDetailViewModelEvent) {
            is ComposeMovieDetailViewModelEvent.GetMovieDetail -> {
                movieId = composeMovieDetailViewModelEvent.movieId
                movieInfo = composeMovieDetailViewModelEvent.movieInfo
                LogUtil.i_dev("리스트에서 영화 정보 가져오기 성공: ${movieId} / ${movieInfo}")
                scopeJob?.cancel()
                scopeJob = scope.launch {
//                    getMovieDetail(movieId = movieId)
                }
                multipleRequest(movieId = movieId)
            }
            is ComposeMovieDetailViewModelEvent.SaveMovieDetail -> {
                scopeJob?.cancel()
                scopeJob = scope.launch {
                    saveMovieDetail(composeMovieDetailViewModelEvent.movieDetail)
                }
            }
            is ComposeMovieDetailViewModelEvent.DeleteMovieDetail -> {
                scopeJob?.cancel()
                scopeJob = scope.launch {
                    deleteMovieDetail(composeMovieDetailViewModelEvent.movieDetail)
                }
            }
        }
    }

    /**
     * 다중 호출 예시
     * 에러 UI 대응은 각 API 호출 함수에서 전달
     *
     * @param movieId
     */
    private fun multipleRequest(movieId: Int) {
        scope.launch {
            getMovieDetail(movieId = movieId + 1)
            LogUtil.i_dev("Middle value: ${movieDetailUiState.value.movieDetail?.originalTitle}")
            if(movieDetailUiState.value.movieDetail?.id == null) {
                coroutineContext.cancel()
            }
            getMovieDetail(movieId = movieDetailUiState.value.movieDetail?.id!! - 1)
        }
    }

    /**
     * 영화 상세정보 요청
     */
    private suspend fun getMovieDetail(movieId: Int) {
//        scopeJob?.cancel()
//        scopeJob = scope.launch {
        getMovieDetailUseCase.invoke(
            movieId = movieId,
            language = language
        )
            .onStart {
                _movieDetailUiEvent.send(element = MovieDetailUiEvent.UpdateLoading(isLoading = true))
            }
            .onCompletion {
                _movieDetailUiEvent.send(element = MovieDetailUiEvent.UpdateLoading(isLoading = false))
            }
            .collect {
                when(it) {
                    is RequestResult.Success -> {
                        val resultData = it.resultData
                        delay(1000L)
                        _movieDetailUiEvent.send(element = MovieDetailUiEvent.UpdateMovieDetail(movieDetail = resultData))
                        checkMovieDetail(movieDetail = resultData!!, null)
                    }
                    is RequestResult.DataEmpty<*> -> {

                    }
                    is RequestResult.Error<*> -> {
                        val errorCode = "${it.code}"
                        val errorMessage = "${it.message}"

                        when(errorCode) {
                            "ERROR" -> {
                                LogUtil.e_dev(errorMessage)
                            }
                        }
                    }
                    is RequestResult.ExceptionError -> {
                        LogUtil.e_dev("${it.throwable}")
                    }
                }
            }

//            getMovieDetailUseCase.invoke(movieId, language)
//                .onStart {
//                    _movieDetailUiEvent.send(element = MovieDetailUiEvent.UpdateLoading(isLoading = true))
//                }
//                .onCompletion {
//                    _movieDetailUiEvent.send(element = MovieDetailUiEvent.UpdateLoading(isLoading = false))
//                }
//                .filter {
//                    val errorCode = "${it.code}"
//                    val errorMessage = "${it.message}"
//
//                    if(it is RequestResult.Error) {
//                        _sideEffectEvent.send(SideEffectEvent.ShowToast(message = errorMessage))
//                        when(errorCode) {
//                            "ERROR" -> {
//                                LogUtil.e_dev("ERROR")
//                            }
//                        }
//                    }
//
//                    if(it is RequestResult.DataEmpty) {
//                    }
//
//                    return@filter it is RequestResult.Success
//                }
//                .catch { e ->
//                    _sideEffectEvent.send(SideEffectEvent.ShowToast(message = e.message?:""))
//                }
//                .map {
//                    it.resultData
//                }
//                .collect {
//                    delay(1000L)
//                    _movieDetailUiEvent.send(element = MovieDetailUiEvent.UpdateMovieDetail(movieDetail = it))
//                    checkMovieDetail(movieDetail = it!!, null)
//                }
//        }
    }

    private suspend fun saveMovieDetail(movieDetail: MovieDetailModel) {
//        scopeJob?.cancel()
//        scopeJob = scope.launch {
            saveMovieDetailUseCase.invoke(movieDetail)
                .onStart {
                    _movieDetailUiEvent.send(element = MovieDetailUiEvent.UpdateLoading(isLoading = true))
                }
                .onCompletion {
                    _movieDetailUiEvent.send(element = MovieDetailUiEvent.UpdateLoading(isLoading = false))
                }
                .filter {
                    val errorCode = "${it.code}"
                    val errorMessage = "${it.message}"

                    if(it is RequestResult.Error) {
                        _sideEffectEvent.send(SideEffectEvent.ShowToast(message = errorMessage))
                        when(errorCode) {
                            "ERROR" -> {
                                LogUtil.e_dev("ERROR")
                            }
                        }
                    }

                    if(it is RequestResult.DataEmpty) {
                    }

                    return@filter it is RequestResult.Success
                }
                .catch { e ->
                    _sideEffectEvent.send(SideEffectEvent.ShowToast(message = e.message?:""))
                }
                .map {
                    it.resultData
                }
                .collect {
                    LogUtil.i_dev("저장 결과: ${it}")
                    checkMovieDetail(movieDetail, true)
                }
//        }
    }

    private suspend fun deleteMovieDetail(movieDetail: MovieDetailModel) {
//        scopeJob?.cancel()
//        scopeJob = scope.launch {
            deleteMovieDetailUseCase.invoke(movieDetail)
                .onStart {
                    _movieDetailUiEvent.send(element = MovieDetailUiEvent.UpdateLoading(isLoading = true))
                }
                .onCompletion {
                    _movieDetailUiEvent.send(element = MovieDetailUiEvent.UpdateLoading(isLoading = false))
                }
                .filter {
                    val errorCode = "${it.code}"
                    val errorMessage = "${it.message}"

                    if(it is RequestResult.Error) {
                        _sideEffectEvent.send(SideEffectEvent.ShowToast(message = errorMessage))
                        when(errorCode) {
                            "ERROR" -> {
                                LogUtil.e_dev("ERROR")
                            }
                        }
                    }

                    if(it is RequestResult.DataEmpty) {
                    }

                    return@filter it is RequestResult.Success
                }
                .catch { e ->
                    _sideEffectEvent.send(SideEffectEvent.ShowToast(message = e.message?:""))
                }
                .map {
                    it.resultData
                }
                .collect {
                    LogUtil.i_dev("삭제 결과: ${it}")
                    checkMovieDetail(movieDetail, false)
                }
//        }
    }

    private suspend fun checkMovieDetail(movieDetail: MovieDetailModel, state: Boolean?) {
//        scopeJob?.cancel()
//        scopeJob = scope.launch {
            checkMovieDetailUseCase.invoke(movieId = movieDetail.id)
                .onStart {
                    _movieDetailUiEvent.send(element = MovieDetailUiEvent.UpdateLoading(isLoading = true))
                }
                .onCompletion {
                    _movieDetailUiEvent.send(element = MovieDetailUiEvent.UpdateLoading(isLoading = false))
                }
                .filter {
                    val errorCode = "${it.code}"
                    val errorMessage = "${it.message}"

                    if(it is RequestResult.Error) {
                        _sideEffectEvent.send(SideEffectEvent.ShowToast(message = errorMessage))
                        when(errorCode) {
                            "ERROR" -> {
                                LogUtil.e_dev("ERROR")
                            }
                        }
                    }

                    if(it is RequestResult.DataEmpty) {
                    }

                    return@filter it is RequestResult.Success
                }
                .catch { e ->
                    _sideEffectEvent.send(SideEffectEvent.ShowToast(message = e.message?:""))
                }
                .map {
                    it.resultData
                }
                .collect {
                    LogUtil.i_dev("확인 결과: ${it} / set state: ${state}")
                    _movieDetailUiEvent.send(element = MovieDetailUiEvent.UpdateSaveState(isSaveState = state?:it!!))
                }
//        }
    }

    override fun onCleared() {
        super.onCleared()

        _sideEffectEvent.close()
    }
    
    sealed interface SideEffectEvent {
        class ShowToast(val message: String): SideEffectEvent
    }
}