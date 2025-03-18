package com.my.mvistudymultimodule.feature.xml.home.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

class NavXmlHomeChangeListener(
    private val navController: NavController
) {
    private fun NavBackStackEntry.lifecycleIsResumed() = this.lifecycle.currentState == Lifecycle.State.RESUMED
}