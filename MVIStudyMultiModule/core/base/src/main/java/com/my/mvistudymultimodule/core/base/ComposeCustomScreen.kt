package com.my.mvistudymultimodule.core.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * backgroundColor - StatusBar 색
 * statusBarPadding - true: 패딩o, false: 패딩x
 * */
@Composable
fun ComposeCustomScreen(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    content: @Composable BoxScope.() -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = backgroundColor
            ),
        content = content
    )

}