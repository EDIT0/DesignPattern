package com.my.mvistudymultimodule.feature.xml.home.event

import androidx.navigation.NavController
import com.my.mvistudymultimodule.feature.xml.home.view.XmlHomeActivity

sealed interface XmlHomeViewModelEvent {
    class SettingNavigation(val activity: XmlHomeActivity, val navController: NavController): XmlHomeViewModelEvent
}