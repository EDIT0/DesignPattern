package com.my.mvistudymultimodule.feature.compose.mainhome.event

sealed interface ComposeMainHomeViewModelEvent {
    class GetPopularMovie(): ComposeMainHomeViewModelEvent
}