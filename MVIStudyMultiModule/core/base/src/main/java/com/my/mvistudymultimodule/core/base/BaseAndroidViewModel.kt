package com.my.mvistudymultimodule.core.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseAndroidViewModel @Inject constructor(
    val app: Application
): AndroidViewModel(application = app) {
}