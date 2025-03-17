package com.my.mvistudymultimodule.feature.xml.event.home

import androidx.navigation.NavController
import com.my.mvistudymultimodule.feature.xml.view.activity.XmlHomeActivity

sealed interface XmlHomeViewModelEvent {
    class SettingNavigation(val activity: XmlHomeActivity, val navController: NavController): XmlHomeViewModelEvent
}