package com.my.mvistudymultimodule.feature.compose.home.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.my.mvistudymultimodule.core.util.LogUtil
import com.my.mvistudymultimodule.feature.compose.home.navigation.ComposeHomeNavHost
import com.my.mvistudymultimodule.feature.compose.home.view.ui.theme.MVIStudyMultiModuleTheme
import com.my.mvistudymultimodule.feature.compose.home.viewmodel.ComposeHomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeHomeActivity : ComponentActivity() {

    private var composeHomeVM: ComposeHomeViewModel? = null
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MVIStudyMultiModuleTheme {
                composeHomeVM = hiltViewModel()
                navController = rememberNavController()

                ScreenTracker(navController = navController)

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        ComposeHomeNavHost(
                            navHostController = navController,
                            composeHomeViewModel = composeHomeVM!!,
                            intent = intent
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        LogUtil.d_dev("${javaClass.simpleName} onResume()")
    }

    override fun onStop() {
        super.onStop()
        LogUtil.d_dev("${javaClass.simpleName} onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.d_dev("${javaClass.simpleName} onDestroy()")
    }
}

@Composable
fun ScreenTracker(navController: NavController) {
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            LogUtil.i_dev("Current route: ${backStackEntry.destination}")
        }
    }
}