package com.my.mvistudymultimodule.feature.xml.home.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.my.mvistudymultimodule.core.base.BaseAndroidViewModel
import com.my.mvistudymultimodule.core.util.NetworkStatusTracker
import com.my.mvistudymultimodule.feature.xml.home.event.XmlHomeViewModelEvent
import com.my.mvistudymultimodule.feature.xml.home.navigation.NavXmlHomeChangeListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class XmlHomeViewModel @Inject constructor(
    app: Application,
    private val networkStatusTracker: NetworkStatusTracker
): BaseAndroidViewModel(app) {

    private val scope = viewModelScope

    private var _navXmlHomeChangeListener: NavXmlHomeChangeListener? = null
    val navXmlHomeChangeListener: NavXmlHomeChangeListener? get() = _navXmlHomeChangeListener
    private var _xmlHomeNavController: NavController? = null
    val xmlHomeNavController: NavController? get() = _xmlHomeNavController

    private val _sideEffectEvent = Channel<SideEffectEvent>()
    val sideEffectEvent = _sideEffectEvent.receiveAsFlow()

    private val _isNetworkConnected = Channel<Boolean>()
    val isNetworkConnected: Flow<Boolean> = _isNetworkConnected.receiveAsFlow()

    init {
        scope.launch {
            networkStatusTracker.networkStatus.collect { status ->
                _isNetworkConnected.send(status)
            }
        }
        scope.launch {
            _isNetworkConnected.send(networkStatusTracker.checkNetworkState())
        }
    }

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