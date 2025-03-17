package com.my.mvistudymultimodule.feature.home.viewmodel

import android.app.Application
import com.my.mvistudymultimodule.core.base.BaseAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    app: Application
): BaseAndroidViewModel(app) {



}