package com.example.mvpexample1.presenter

import androidx.lifecycle.MutableLiveData
import com.example.mvpexample1.model.data.MovieModel
import com.example.mvpexample1.model.db.MovieDao
import com.example.mvpexample1.model.network.ApiService
import com.example.mvpexample1.model.usecase.DeleteMovieUseCase
import com.example.mvpexample1.model.usecase.GetAllSavedMoviesUseCase

class SavedMoviesPresenter(
    private val savedContractView: SavedMoviesContract.View,
    private val getAllSavedMoviesUseCase: GetAllSavedMoviesUseCase,
    private val deleteMovieUseCase: DeleteMovieUseCase
) : SavedMoviesContract.Presenter {

    val savedMovieList = MutableLiveData<ArrayList<MovieModel.MovieModelResult>>()
    override suspend fun getSavedMovies() {
        try {
            val result = getAllSavedMoviesUseCase.execute()

            result.collect {
                savedMovieList.postValue(it as ArrayList)
            }

        } catch (e : Exception) {

        }
    }

    override suspend fun deleteMovie(data: MovieModel.MovieModelResult) {
        deleteMovieUseCase.execute(data)
    }

    override fun releaseView() {
        savedContractView.showToast("releaseView")
    }
}