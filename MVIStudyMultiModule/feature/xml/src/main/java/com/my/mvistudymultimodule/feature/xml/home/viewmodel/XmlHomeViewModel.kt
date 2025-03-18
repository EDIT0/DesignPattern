package com.my.mvistudymultimodule.feature.xml.home.viewmodel

import android.app.Application
import androidx.navigation.NavController
import com.my.mvistudymultimodule.core.base.BaseAndroidViewModel
import com.my.mvistudymultimodule.feature.xml.home.event.XmlHomeViewModelEvent
import com.my.mvistudymultimodule.feature.xml.home.navigation.NavXmlHomeChangeListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class XmlHomeViewModel @Inject constructor(
    app: Application
): BaseAndroidViewModel(app) {

    private var _navXmlHomeChangeListener: NavXmlHomeChangeListener? = null
    val navXmlHomeChangeListener: NavXmlHomeChangeListener? get() = _navXmlHomeChangeListener
    private var _xmlHomeNavController: NavController? = null
    val xmlHomeNavController: NavController? get() = _xmlHomeNavController

    private val _sideEffectEvent = MutableSharedFlow<SideEffectEvent>()
    val sideEffectEvent = _sideEffectEvent.asSharedFlow()

    fun handleViewModelEvent(xmlHomeViewModelEvent: XmlHomeViewModelEvent) {
        when(xmlHomeViewModelEvent) {
            is XmlHomeViewModelEvent.SettingNavigation -> {
//                val splashNavFragment = xmlHomeViewModelEvent.activity.supportFragmentManager.findFragmentById(R.id.navFragment) as NavHostFragment
//                _permissionNavController = splashNavFragment.navController
//                _permissionNavController?.setGraph(R.navigation.nav_permission)
                _xmlHomeNavController = xmlHomeViewModelEvent.navController
                _navXmlHomeChangeListener = NavXmlHomeChangeListener(_xmlHomeNavController!!)
            }
        }
    }

    enum class SideEffectEvent {

    }

}