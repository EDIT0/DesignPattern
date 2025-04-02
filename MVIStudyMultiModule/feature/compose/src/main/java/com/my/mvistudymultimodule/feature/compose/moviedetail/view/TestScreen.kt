package com.my.mvistudymultimodule.feature.compose.moviedetail.view

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.my.mvistudymultimodule.feature.compose.home.viewmodel.ComposeHomeViewModel

@Composable
fun TestScreen(
    navController: NavController,
    composeHomeViewModel: ComposeHomeViewModel,
    intent: Intent,
) {
    val localContext = LocalContext.current
    val scope = rememberCoroutineScope()

    val initExecute = rememberSaveable {
        mutableStateOf(true)
    }

    BackHandler {
        if(navController.previousBackStackEntry != null) {
            navController.popBackStack()
        }
    }
}