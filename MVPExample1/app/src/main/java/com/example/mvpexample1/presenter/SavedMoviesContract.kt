package com.example.mvpexample1.presenter

import com.example.mvpexample1.model.data.MovieModel

class SavedMoviesContract {
    interface View : BaseView<Presenter> {
        suspend fun showProgress(value: Boolean)
    }

    interface Presenter : BasePresenter<View> {
        suspend fun getSavedMovies()
        suspend fun deleteMovie(data: MovieModel.MovieModelResult)
    }
}