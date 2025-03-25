package com.my.mvistudymultimodule.feature.compose.home.navigation

import android.content.Intent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.my.mvistudymultimodule.core.base.NavigationScreenName
import com.my.mvistudymultimodule.feature.compose.home.viewmodel.ComposeHomeViewModel
import com.my.mvistudymultimodule.feature.compose.mainhome.view.ComposeMainHomeScreen

@Composable
fun ComposeHomeNavHost(
    navHostController: NavHostController,
    composeHomeViewModel: ComposeHomeViewModel,
    startDestination: String = NavigationScreenName.ComposeMainHomeScreen.name,
    intent: Intent
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        exitTransition = {
            ExitTransition.None
        },
        enterTransition = {
            EnterTransition.None
        }
    ) {
        /* ComposeMainHomeScreen */
        composable(
            route = NavigationScreenName.ComposeMainHomeScreen.name
        ) {
            ComposeMainHomeScreen(
                navController = navHostController,
                composeHomeViewModel = composeHomeViewModel,
                intent = intent
            )
        }
    }
}