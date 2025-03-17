package com.my.mvistudymultimodule.feature.xml.event.mainhome

sealed interface XmlMainHomeViewModelEvent {
    class Test(val str: String): XmlMainHomeViewModelEvent
}