package com.example.mvpexample1.presenter

interface BasePresenter<T> {
//    fun setView(view: T)
    fun releaseView()
}