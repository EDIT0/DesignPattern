package com.my.mvistudymultimodule.core.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun dpToSp(dp: Dp) = with(LocalDensity.current) { dp.toSp() }