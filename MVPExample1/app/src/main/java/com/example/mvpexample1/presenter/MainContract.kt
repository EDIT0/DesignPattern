package com.example.mvpexample1.presenter

import com.example.mvpexample1.model.data.MovieModel

class MainContract {
    interface View : BaseView<Presenter> {
        suspend fun showProgress(value: Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun setSearchQuery(query: String)
        fun getLoadingState() : Boolean
        fun getTotalPages() : Int
        fun getCurrentPage() : Int
        fun setPage(page : Int)
        suspend fun searchMovies()
        suspend fun saveMovie(data: MovieModel.MovieModelResult)
    }
}