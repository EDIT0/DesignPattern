package com.my.mvistudymultimodule.feature.compose.home.navigation

import android.content.Intent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.my.mvistudymultimodule.core.base.INTENT_KEY_MOVIE_ID
import com.my.mvistudymultimodule.core.base.NavigationScreenName
import com.my.mvistudymultimodule.core.util.LogUtil
import com.my.mvistudymultimodule.feature.compose.home.viewmodel.ComposeHomeViewModel
import com.my.mvistudymultimodule.feature.compose.mainhome.view.ComposeMainHomeScreen
import com.my.mvistudymultimodule.feature.compose.moviedetail.view.ComposeMovieDetailScreen
import com.my.mvistudymultimodule.feature.compose.moviedetail.view.TestScreen

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

        /* ComposeMovieDetailScreen */
        composable(
            route = NavigationScreenName.ComposeMovieDetailScreen.name + "/{${INTENT_KEY_MOVIE_ID}}",
            arguments = listOf(
                navArgument(
                    name = INTENT_KEY_MOVIE_ID
                ) {
                    type = NavType.IntType
                }
            )
        ) {
            val movieId = it.arguments?.getInt(INTENT_KEY_MOVIE_ID)

            if(movieId == null) {
                navHostController.popBackStack()
            } else {
                ComposeMovieDetailScreen(
                    navController = navHostController,
                    composeHomeViewModel = composeHomeViewModel,
                    intent = intent,
                    movieId = movieId
                )
            }
        }

        /* TestScreen */
        composable(
            route = NavigationScreenName.TestScreen.name
        ) {
            TestScreen(
                navController = navHostController,
                composeHomeViewModel = composeHomeViewModel,
                intent = intent
            )
        }
    }
}