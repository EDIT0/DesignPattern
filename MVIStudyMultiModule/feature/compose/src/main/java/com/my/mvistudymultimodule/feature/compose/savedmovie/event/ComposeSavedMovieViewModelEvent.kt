package com.my.mvistudymultimodule.feature.compose.savedmovie.event

sealed interface ComposeSavedMovieViewModelEvent {

    class GetSavedMovie(): ComposeSavedMovieViewModelEvent

}