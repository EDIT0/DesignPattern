package com.example.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.MovieModelResult
import com.example.domain.usecase.SaveMovieUseCase
import kotlinx.coroutines.launch

class MovieInfoViewModel(
    private val app : Application,
    private val saveMovieUseCase: SaveMovieUseCase
) : AndroidViewModel(app){

    //local data
    fun saveMovie(movieModelResult: MovieModelResult) {
        viewModelScope.launch {
            saveMovieUseCase.execute(movieModelResult)
        }
    }
}