package com.my.mvistudymultimodule.feature.xml.viewmodel

import android.app.Application
import com.my.mvistudymultimodule.core.base.BaseAndroidViewModel
import com.my.mvistudymultimodule.core.util.LogUtil
import com.my.mvistudymultimodule.feature.xml.event.mainhome.XmlMainHomeViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class XmlMainHomeViewModel @Inject constructor(
    app: Application
): BaseAndroidViewModel(app) {


    fun handleViewModelEvent(xmlMainHomeViewModelEvent: XmlMainHomeViewModelEvent) {
        when(xmlMainHomeViewModelEvent) {
            is XmlMainHomeViewModelEvent.Test -> {
                LogUtil.d_dev("${xmlMainHomeViewModelEvent.str}")
            }
        }
    }

}