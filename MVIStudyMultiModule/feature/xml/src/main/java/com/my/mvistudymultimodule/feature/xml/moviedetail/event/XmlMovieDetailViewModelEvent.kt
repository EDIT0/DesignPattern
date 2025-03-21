package com.my.mvistudymultimodule.feature.xml.moviedetail.event

sealed interface XmlMovieDetailViewModelEvent {
    class SetMovieId(val movieId: Int): XmlMovieDetailViewModelEvent
    class GetMovieDetail(): XmlMovieDetailViewModelEvent
}